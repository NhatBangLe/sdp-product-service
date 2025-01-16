package io.github.nhatbangle.sdp.product.dto.response;

import jakarta.annotation.Nullable;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.product.entity.product.Product}
 */
public record ProductResponse(
        String id,
        boolean isUsed,
        String name,
        @Nullable String description,
        long createdAtMillis,
        @Nullable Long updatedAtMillis
) implements Serializable {
}