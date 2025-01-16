package io.github.nhatbangle.sdp.product.dto.response;

import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link io.github.nhatbangle.sdp.product.entity.product.ProductChangelog}
 */
public record ProductChangelogResponse(
        String id,
        String title,
        @Nullable String description,
        long createdAtMillis,
        @Nullable Long updatedAtMillis,
        @Nullable List<AttachmentResponse> attachments
) implements Serializable {
}