package io.github.nhatbangle.sdp.product.dto.request.module;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.product.entity.module.Module}
 */
public record ModuleUpdatingRequest(
        @NotBlank @Size(max = 150) String name,
        @Nullable String description
) implements Serializable {
}