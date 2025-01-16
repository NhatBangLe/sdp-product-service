package io.github.nhatbangle.sdp.product.mapper;

import io.github.nhatbangle.sdp.product.dto.response.DocumentLabelResponse;
import io.github.nhatbangle.sdp.product.entity.DocumentLabel;

import java.util.Objects;

public final class DocumentLabelMapper implements IEntityMapper<DocumentLabel, DocumentLabelResponse> {

    @Override
    public DocumentLabelResponse toResponse(DocumentLabel entity) {
        var updatedAt = entity.getUpdatedAt();
        return new DocumentLabelResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                Objects.requireNonNull(entity.getCreatedAt()).toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null,
                entity.getColor()
        );
    }

}
