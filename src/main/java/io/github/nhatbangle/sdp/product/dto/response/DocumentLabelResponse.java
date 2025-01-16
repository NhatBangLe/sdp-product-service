package io.github.nhatbangle.sdp.product.dto.response;

import io.github.nhatbangle.sdp.product.entity.DocumentLabel;
import jakarta.annotation.Nullable;

import java.io.Serializable;

/**
 * DTO response for {@link DocumentLabel}
 */
public record DocumentLabelResponse(
        String id,
        String name,
        @Nullable String description,
        long createdAtMillis,
        @Nullable Long updatedAtMillis,
        String color
) implements Serializable {
}