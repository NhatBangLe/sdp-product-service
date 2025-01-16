package io.github.nhatbangle.sdp.product.dto.request.module;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.product.entity.module.Module}
 */
public record ModuleCreatingRequest(
        @NotBlank @Size(max = 150) String name,
        @Nullable String description,
        @UUID @NotNull String productVersionId
) implements Serializable {
}