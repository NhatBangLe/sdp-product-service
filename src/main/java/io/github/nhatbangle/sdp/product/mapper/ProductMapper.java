package io.github.nhatbangle.sdp.product.mapper;

import io.github.nhatbangle.sdp.product.dto.response.*;
import io.github.nhatbangle.sdp.product.entity.product.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;

@Validated
@RequiredArgsConstructor
public class ProductMapper implements IEntityMapper<Product, ProductResponse> {

    private final DocumentLabelMapper mapper;

    @Override
    public @NotNull ProductResponse toResponse(@NotNull Product entity) {
        var updatedAt = entity.getUpdatedAt();
        return new ProductResponse(
                entity.getId(),
                entity.getIsUsed(),
                entity.getName(),
                entity.getDescription(),
                Objects.requireNonNull(entity.getCreatedAt()).toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null
        );
    }

    @NotNull
    public ProductVersionResponse toResponse(@NotNull ProductVersion version) {
        var updatedAt = version.getUpdatedAt();
        return new ProductVersionResponse(
                version.getId(),
                version.getName(),
                version.getIsUsed(),
                Objects.requireNonNull(version.getCreatedAt()).toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null
        );
    }

    @NotNull
    public ProductChangelogResponse toResponse(@NotNull ProductChangelog changelog) {
        var updatedAt = changelog.getUpdatedAt();

        var attachments = changelog.getAttachments();
        var attachmentResponses = attachments != null ? attachments.parallelStream()
                .map(obj -> new AttachmentResponse(
                        obj.getAttachment().getId(),
                        Objects.requireNonNull(obj.getCreatedAt()).toEpochMilli()))
                .sorted((a, b) -> Math.toIntExact(a.createdAtMillis() - b.createdAtMillis()))
                .toList() : null;

        return new ProductChangelogResponse(
                changelog.getId(),
                changelog.getTitle(),
                changelog.getDescription(),
                Objects.requireNonNull(changelog.getCreatedAt()).toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null,
                attachmentResponses
        );
    }

    @NotNull
    public ProductDocumentResponse toResponse(@NotNull ProductDocument document) {
        var updatedAt = document.getUpdatedAt();
        var product = document.getProduct();

        var labels = document.getLabels();
        List<DocumentLabelResponse> labelResponses = null;
        if (labels != null)
            labelResponses = labels.stream()
                    .map(obj -> mapper.toResponse(obj.getLabel()))
                    .toList();

        var attachments = document.getAttachments();
        List<AttachmentResponse> attachmentResponses = null;
        if (attachments != null)
            attachmentResponses = attachments.stream().map(obj -> new AttachmentResponse(
                    obj.getAttachment().getId(),
                    Objects.requireNonNull(obj.getCreatedAt()).toEpochMilli())
            ).toList();

        return new ProductDocumentResponse(
                document.getId(),
                document.getTitle(),
                document.getDescription(),
                Objects.requireNonNull(document.getCreatedAt()).toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null,
                product != null ? product.getId() : null,
                labelResponses,
                attachmentResponses
        );
    }

}
