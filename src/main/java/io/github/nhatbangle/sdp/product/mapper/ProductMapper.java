package io.github.nhatbangle.sdp.product.mapper;

import io.github.nhatbangle.sdp.product.dto.response.*;
import io.github.nhatbangle.sdp.product.entity.product.*;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public final class ProductMapper implements IEntityMapper<Product, ProductResponse> {

    private final DocumentLabelMapper mapper;

    @Override
    public ProductResponse toResponse(Product entity) {
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

    public ProductVersionResponse toResponse(ProductVersion version) {
        var updatedAt = version.getUpdatedAt();
        return new ProductVersionResponse(
                version.getId(),
                version.getName(),
                version.getIsUsed(),
                Objects.requireNonNull(version.getCreatedAt()).toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null
        );
    }

    public ProductChangelogResponse toResponse(ProductChangelog changelog) {
        var updatedAt = changelog.getUpdatedAt();
        var attachmentIds = Objects.requireNonNullElse(
                        changelog.getAttachments(),
                        new HashSet<ProductChangelogHasAttachment>())
                .stream()
                .map(obj -> new AttachmentResponse(
                        obj.getAttachment().getId(),
                        Objects.requireNonNull(obj.getCreatedAt()).toEpochMilli()))
                .sorted((a, b) -> Math.toIntExact(a.createdAtMillis() - b.createdAtMillis()))
                .toList();
        return new ProductChangelogResponse(
                changelog.getId(),
                changelog.getTitle(),
                changelog.getDescription(),
                Objects.requireNonNull(changelog.getCreatedAt()).toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null,
                attachmentIds
        );
    }

    public ProductDocumentResponse toResponse(ProductDocument document) {
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
