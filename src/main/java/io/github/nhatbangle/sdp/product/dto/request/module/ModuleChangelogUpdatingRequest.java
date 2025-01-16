package io.github.nhatbangle.sdp.product.dto.request.module;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link io.github.nhatbangle.sdp.product.entity.module.ModuleChangelog}
 */
public record ModuleChangelogUpdatingRequest(
        @Size(max = 150) @NotBlank String title,
        @Nullable String description,
        @Nullable Set<String> attachmentIds
) implements Serializable {
}