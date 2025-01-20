package io.github.nhatbangle.sdp.product.service.product;

import io.github.nhatbangle.sdp.product.dto.request.product.ProductDocumentCreatingRequest;
import io.github.nhatbangle.sdp.product.dto.request.product.ProductDocumentUpdatingRequest;
import io.github.nhatbangle.sdp.product.entity.Attachment;
import io.github.nhatbangle.sdp.product.entity.DocumentLabel;
import io.github.nhatbangle.sdp.product.entity.id.ProductDocumentHasAttachmentId;
import io.github.nhatbangle.sdp.product.entity.id.ProductDocumentHasDocumentLabelId;
import io.github.nhatbangle.sdp.product.entity.product.ProductDocument;
import io.github.nhatbangle.sdp.product.entity.product.ProductDocumentHasAttachment;
import io.github.nhatbangle.sdp.product.entity.product.ProductDocumentHasDocumentLabel;
import io.github.nhatbangle.sdp.product.exception.DataConflictException;
import io.github.nhatbangle.sdp.product.exception.ServiceUnavailableException;
import io.github.nhatbangle.sdp.product.repository.product.ProductDocumentRepository;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
@CacheConfig(cacheNames = "product-documents")
public class ProductDocumentService {

    private final MessageSource messageSource;
    private final UserService userService;
    private final ProductDocumentRepository documentRepository;
    private final ProductService productService;
    private final AttachmentService attachmentService;

    @NotNull
    public Page<ProductDocument> queryAllDocuments(
            @NotNull @UUID String userId,
            @Nullable @UUID String productName,
            @Nullable String documentTitle,
            @NotNull Pageable pageable
    ) {
        return documentRepository.findAllByUser_IdAndProduct_NameAndTitleContainsIgnoreCase(
                userId,
                productName,
                documentTitle,
                pageable
        );
    }

    @NotNull
    @Cacheable(key = "#documentId")
    public ProductDocument getDocument(@NotNull @UUID String documentId)
            throws IllegalArgumentException {
        return findDocument(documentId);
    }

    private ProductDocument findDocument(String documentId) throws IllegalArgumentException {
        return documentRepository.findById(documentId).orElseThrow(() -> {
            var message = messageSource.getMessage(
                    "product_document.not_found",
                    new Object[]{documentId},
                    Locale.getDefault()
            );
            return new IllegalArgumentException(message);
        });
    }

    @NotNull
    public ProductDocument createDocument(@NotNull @Valid ProductDocumentCreatingRequest request)
            throws IllegalArgumentException, ServiceUnavailableException {
        var user = userService.getUserById(request.userId());
        var documentBuilder = ProductDocument.builder()
                .title(request.title())
                .description(request.description())
                .user(user);
        var productId = request.productId();
        if (productId != null) {
            try {
                var product = productService.getProduct(productId);
                documentBuilder.product(product);
            } catch (IllegalArgumentException e) {
                documentBuilder.product(null);
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

    private Set<ProductDocumentHasAttachment> convertIdToAttachment(
            @NotNull ProductDocument document,
            @NotNull Set<String> attachmentIds
    ) throws IllegalArgumentException, DataConflictException {
        var savedDocumentId = document.getId();
        if (savedDocumentId == null) {
            var message = messageSource.getMessage(
                    "product_document.not_saved",
                    null,
                    Locale.getDefault()
            );
            throw new DataConflictException(message);
        }

        attachmentService.validateIds(attachmentIds);
        return attachmentIds.parallelStream()
                .map(atmId -> {
                    var id = ProductDocumentHasAttachmentId.builder()
                            .attachmentId(atmId)
                            .documentId(savedDocumentId)
                            .build();
                    var attachment = Attachment.builder().id(atmId).build();
                    return ProductDocumentHasAttachment.builder()
                            .id(id)
                            .document(document)
                            .attachment(attachment)
                            .build();
                })
                .collect(Collectors.toSet());
    }

    private Set<ProductDocumentHasDocumentLabel> convertIdToLabel(
            @NotNull ProductDocument document,
            @NotNull List<String> labelIds
    ) throws IllegalArgumentException, DataConflictException {
        var savedDocumentId = document.getId();
        if (savedDocumentId == null) {
            var message = messageSource.getMessage(
                    "product_document.not_saved",
                    null,
                    Locale.getDefault()
            );
            throw new DataConflictException(message);
        }

        var arrSize = labelIds.size();
        var labels = new HashSet<ProductDocumentHasDocumentLabel>(arrSize);

        for (int i = 0; i < arrSize; i++) {
            var labelId = labelIds.get(i);
            var id = ProductDocumentHasDocumentLabelId.builder()
                    .labelId(labelId)
                    .documentId(savedDocumentId)
                    .build();
            var label = ProductDocumentHasDocumentLabel.builder()
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
    public ProductDocument updateDocument(
            @NotNull @UUID String documentId,
            @NotNull @Valid ProductDocumentUpdatingRequest request
    ) throws IllegalArgumentException {
        var document = findDocument(documentId);

        document.setTitle(request.title());
        document.setDescription(request.description());

        var productId = request.productId();
        if (productId != null) {
            try {
                var product = productService.getProduct(productId);
                document.setProduct(product);
            } catch (IllegalArgumentException e) {
                document.setProduct(null);
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
