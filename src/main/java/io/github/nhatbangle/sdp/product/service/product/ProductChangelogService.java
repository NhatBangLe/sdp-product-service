package io.github.nhatbangle.sdp.product.service.product;

import io.github.nhatbangle.sdp.product.dto.request.product.ProductChangelogUpdatingRequest;
import io.github.nhatbangle.sdp.product.dto.request.product.ProductChangelogCreatingRequest;
import io.github.nhatbangle.sdp.product.entity.Attachment;
import io.github.nhatbangle.sdp.product.entity.id.ProductChangelogHasAttachmentId;
import io.github.nhatbangle.sdp.product.entity.product.ProductChangelog;
import io.github.nhatbangle.sdp.product.entity.product.ProductChangelogHasAttachment;
import io.github.nhatbangle.sdp.product.exception.DataConflictException;
import io.github.nhatbangle.sdp.product.exception.ServiceUnavailableException;
import io.github.nhatbangle.sdp.product.repository.product.ProductChangelogRepository;
import io.github.nhatbangle.sdp.product.service.AttachmentService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
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
public class ProductChangelogService {

    private final MessageSource messageSource;
    private final ProductChangelogRepository changelogRepository;
    private final ProductVersionService productVersionService;
    private final AttachmentService attachmentService;

    @NotNull
    public Page<ProductChangelog> queryAllChangelogs(
            @NotNull @UUID String productVersionId,
            @Nullable String title,
            @NotNull Pageable pageable
    ) {
        return changelogRepository.findAllByProductVersion_IdAndTitleContainsIgnoreCase(
                productVersionId,
                Objects.requireNonNullElse(title, ""),
                pageable
        );
    }

    @NotNull
    public ProductChangelog getChangelog(@NotNull @UUID String changelogId)
            throws IllegalArgumentException {
        return changelogRepository.findById(changelogId).orElseThrow(() -> {
            var message = messageSource.getMessage(
                    "product_changelog.not_found",
                    new Object[]{changelogId},
                    Locale.getDefault()
            );
            return new IllegalArgumentException(message);
        });
    }

    @NotNull
    public ProductChangelog createChangelog(
            @NotNull @UUID String productVersionId,
            @NotNull @Valid ProductChangelogCreatingRequest request
    ) throws IllegalArgumentException, ServiceUnavailableException {
        var productVersion = productVersionService.getVersion(productVersionId);
        var changelog = changelogRepository.save(ProductChangelog.builder()
                .title(request.title())
                .description(request.description())
                .productVersion(productVersion)
                .build());

        var attachmentIds = request.attachmentIds();
        if (attachmentIds != null) {
            changelog.setAttachments(convertIdToChangelogAttachment(changelog, attachmentIds));
            return changelogRepository.save(changelog);
        }

        return changelog;
    }

    public void updateChangelog(
            @NotNull @UUID String changelogId,
            @NotNull @Valid ProductChangelogUpdatingRequest request
    ) {
        var changelog = getChangelog(changelogId);
        changelog.setTitle(request.title());
        changelog.setDescription(request.description());
        var attachmentIds = request.attachmentIds();
        if (attachmentIds != null)
            changelog.setAttachments(convertIdToChangelogAttachment(changelog, attachmentIds));
        changelogRepository.save(changelog);
    }

    public void deleteChangelog(@NotNull @UUID String changelogId) {
        var product = getChangelog(changelogId);
        changelogRepository.delete(product);
    }

    private Set<ProductChangelogHasAttachment> convertIdToChangelogAttachment(
            @NotNull ProductChangelog changelog,
            @NotNull Set<String> attachmentIds
    ) {
        // validate attachment ids
        attachmentService.validateIds(attachmentIds);

        var changelogId = changelog.getId();
        if (changelogId == null) {
            var message = messageSource.getMessage(
                    "product_changelog.not_saved",
                    null,
                    Locale.getDefault()
            );
            throw new DataConflictException(message);
        }

        return attachmentIds.stream().map(attachmentId -> {
                    var id = ProductChangelogHasAttachmentId.builder()
                            .attachmentId(attachmentId)
                            .changelogId(changelogId)
                            .build();
                    var attachment = Attachment.builder().id(attachmentId).build();

                    return ProductChangelogHasAttachment.builder()
                            .id(id)
                            .changelog(changelog)
                            .attachment(attachment)
                            .build();
                }
        ).collect(Collectors.toSet());
    }

}
