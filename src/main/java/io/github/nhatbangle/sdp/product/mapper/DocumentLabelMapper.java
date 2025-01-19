package io.github.nhatbangle.sdp.product.mapper;

import io.github.nhatbangle.sdp.product.dto.response.DocumentLabelResponse;
import io.github.nhatbangle.sdp.product.entity.DocumentLabel;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

@Validated
public final class DocumentLabelMapper implements IEntityMapper<DocumentLabel, DocumentLabelResponse> {

    @Override
    public @NotNull DocumentLabelResponse toResponse(@NotNull DocumentLabel entity) {
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
