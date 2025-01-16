package io.github.nhatbangle.sdp.product.dto.request.product;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link io.github.nhatbangle.sdp.product.entity.product.ProductChangelog}
 */
public record ProductChangelogUpdatingRequest(
        @Size(max = 150) @NotBlank String title,
        @Nullable String description,
        @Nullable Set<String> attachmentIds
) implements Serializable {
}