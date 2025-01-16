package io.github.nhatbangle.sdp.product.dto.response;

import jakarta.annotation.Nullable;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.product.entity.product.ProductVersion}
 */
public record ProductVersionResponse(
        String id,
        String versionName,
        boolean isUsed,
        long createdAtMillis,
        @Nullable Long updatedAtMillis
) implements Serializable {
}