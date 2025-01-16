package io.github.nhatbangle.sdp.product.dto.response;

import io.github.nhatbangle.sdp.product.entity.module.Module;
import jakarta.annotation.Nullable;

import java.io.Serializable;

/**
 * DTO for {@link Module}
 */
public record ModuleResponse(
        String id,
        boolean isUsed,
        String name,
        @Nullable String description,
        long createdAtMillis,
        @Nullable Long updatedAtMillis
) implements Serializable {
}