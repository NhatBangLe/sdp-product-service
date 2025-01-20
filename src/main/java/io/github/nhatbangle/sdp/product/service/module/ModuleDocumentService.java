package io.github.nhatbangle.sdp.product.service.module;

import io.github.nhatbangle.sdp.product.dto.request.module.ModuleDocumentCreatingRequest;
import io.github.nhatbangle.sdp.product.dto.request.module.ModuleDocumentUpdatingRequest;
import io.github.nhatbangle.sdp.product.entity.Attachment;
import io.github.nhatbangle.sdp.product.entity.DocumentLabel;
import io.github.nhatbangle.sdp.product.entity.id.ModuleDocumentHasAttachmentId;
import io.github.nhatbangle.sdp.product.entity.id.ModuleDocumentHasDocumentLabelId;
import io.github.nhatbangle.sdp.product.entity.module.ModuleDocument;
import io.github.nhatbangle.sdp.product.entity.module.ModuleDocumentHasAttachment;
import io.github.nhatbangle.sdp.product.entity.module.ModuleDocumentHasDocumentLabel;
import io.github.nhatbangle.sdp.product.exception.DataConflictException;
import io.github.nhatbangle.sdp.product.exception.ServiceUnavailableException;
import io.github.nhatbangle.sdp.product.repository.module.ModuleDocumentRepository;
import io.github.nhatbangle.sdp.product.service.AttachmentService;
import io.github.nhatbangle.sdp.product.service.UserService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
@CacheConfig(cacheNames = "module-documents")
public class ModuleDocumentService {

    private final MessageSource messageSource;
    private final UserService userService;
    private final ModuleDocumentRepository documentRepository;
    private final ModuleService moduleService;
    private final AttachmentService attachmentService;

    @NotNull
    public Page<ModuleDocument> queryAllDocuments(
            @NotNull @UUID String userId,
            @Nullable @UUID String moduleName,
            @Nullable String documentTitle,
            @NotNull Pageable pageable
    ) {
        return documentRepository.findAllByUser_IdAndModule_NameContainsIgnoreCaseAndTitleContainsIgnoreCase(
                userId,
                moduleName,
                documentTitle,
                pageable
        );
    }

    @NotNull
    @Cacheable(key = "#documentId")
    public ModuleDocument getDocument(@NotNull @UUID String documentId)
            throws IllegalArgumentException {
        return findDocument(documentId);
    }

    private ModuleDocument findDocument(String documentId) throws IllegalArgumentException {
        return documentRepository.findById(documentId).orElseThrow(() -> {
            var message = messageSource.getMessage(
                    "module_document.not_found",
                    new Object[]{documentId},
                    Locale.getDefault()
            );
            return new IllegalArgumentException(message);
        });
    }

    @NotNull
    public ModuleDocument createDocument(@NotNull @Valid ModuleDocumentCreatingRequest request)
            throws IllegalArgumentException, ServiceUnavailableException {
        var user = userService.getUserById(request.userId());
        var documentBuilder = ModuleDocument.builder()
                .title(request.title())
                .description(request.description())
                .user(user);
        var moduleId = request.moduleId();
        if (moduleId != null) {
            try {
                var module = moduleService.getModule(moduleId);
                documentBuilder.module(module);
            } catch (IllegalArgumentException e) {
                documentBuilder.module(null);
            }
        }

        var isUpdated = false;
        var savedDocument = documentRepository.save(documentBuilder.build());

        var attachmentIds = request.attachmentIds();
        if (attachmentIds != null && !attachmentIds.isEmpty()) {
            savedDocument.setAttachments(convertIdToAttachment(savedDocument, attachmentIds));
            isUpdated = true;
        }

        var labelIds = request.labelIds();
        if (labelIds != null && !labelIds.isEmpty()) {
            savedDocument.setLabels(convertIdToLabel(savedDocument, labelIds));
            isUpdated = true;
        }

        return isUpdated ? documentRepository.save(savedDocument) : savedDocument;
    }

    private Set<ModuleDocumentHasAttachment> convertIdToAttachment(
            @NotNull ModuleDocument document,
            @NotNull Set<String> attachmentIds
    ) throws IllegalArgumentException, DataConflictException {
        var savedDocumentId = document.getId();
        if (savedDocumentId == null) {
            var message = messageSource.getMessage(
                    "module_document.not_saved",
                    null,
                    Locale.getDefault()
            );
            throw new DataConflictException(message);
        }

        attachmentService.validateIds(attachmentIds);
        return attachmentIds.parallelStream()
                .map(atmId -> {
                    var id = ModuleDocumentHasAttachmentId.builder()
                            .attachmentId(atmId)
                            .documentId(savedDocumentId)
                            .build();
                    var attachment = Attachment.builder().id(atmId).build();
                    return ModuleDocumentHasAttachment.builder()
                            .id(id)
                            .document(document)
                            .attachment(attachment)
                            .build();
                })
                .collect(Collectors.toSet());
    }

    private Set<ModuleDocumentHasDocumentLabel> convertIdToLabel(
            @NotNull ModuleDocument document,
            @NotNull List<String> labelIds
    ) throws IllegalArgumentException, DataConflictException {
        var savedDocumentId = document.getId();
        if (savedDocumentId == null) {
            var message = messageSource.getMessage(
                    "module_document.not_saved",
                    null,
                    Locale.getDefault()
            );
            throw new DataConflictException(message);
        }

        var arrSize = labelIds.size();
        var labels = new HashSet<ModuleDocumentHasDocumentLabel>(arrSize);

        for (int i = 0; i < arrSize; i++) {
            var labelId = labelIds.get(i);
            var id = ModuleDocumentHasDocumentLabelId.builder()
                    .labelId(labelId)
                    .documentId(savedDocumentId)
                    .build();
            var label = ModuleDocumentHasDocumentLabel.builder()
                    .id(id)
                    .document(document)
                    .numOrder((short) i)
                    .label(DocumentLabel.builder().id(labelId).build())
                    .build();
            labels.add(label);
        }
        return labels;
    }

    @NotNull
    @CachePut(key = "#documentId")
    public ModuleDocument updateDocument(
            @NotNull @UUID String documentId,
            @NotNull @Valid ModuleDocumentUpdatingRequest request
    ) throws IllegalArgumentException {
        var document = findDocument(documentId);

        document.setTitle(request.title());
        document.setDescription(request.description());

        var moduleId = request.moduleId();
        if (moduleId != null) {
            try {
                var product = moduleService.getModule(moduleId);
                document.setModule(product);
            } catch (IllegalArgumentException e) {
                document.setModule(null);
            }
        }

        var attachmentIds = request.attachmentIds();
        if (attachmentIds != null && !attachmentIds.isEmpty()) {
            document.setAttachments(convertIdToAttachment(document, attachmentIds));
        }

        var labelIds = request.labelIds();
        if (labelIds != null && !labelIds.isEmpty()) {
            document.setLabels(convertIdToLabel(document, labelIds));
        }

        return documentRepository.save(document);
    }

    @CacheEvict(key = "#documentId")
    public void deleteDocument(@NotNull @UUID String documentId) {
        documentRepository.deleteById(documentId);
    }

}
