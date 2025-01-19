package io.github.nhatbangle.sdp.product.dto.request.product;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link io.github.nhatbangle.sdp.product.entity.product.ProductChangelog}
 */
public record ProductChangelogCreatingRequest(
        @NotBlank @Size(max = 150) String title,
        @Nullable String description,
        @Nullable Set<String> attachmentIds,
        @NotNull @UUID String productVersionId
) implements Serializable {
}