package io.github.nhatbangle.sdp.product.dto.request.module;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.product.entity.module.ModuleVersion}
 */
public record ModuleVersionCreatingRequest(
        @Size(max = 255) @NotBlank String versionName,
        @UUID @NotNull String moduleId
) implements Serializable {
}