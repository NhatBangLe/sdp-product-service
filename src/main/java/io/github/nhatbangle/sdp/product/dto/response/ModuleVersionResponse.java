package io.github.nhatbangle.sdp.product.dto.response;

import jakarta.annotation.Nullable;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.product.entity.module.ModuleVersion}
 */
public record ModuleVersionResponse(
        String id,
        String name,
        boolean isUsed,
        long createdAtMillis,
        @Nullable Long updatedAtMillis
) implements Serializable {
}