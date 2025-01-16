package io.github.nhatbangle.sdp.product.dto.request.module;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.product.entity.module.ModuleVersion}
 */
public record ModuleVersionUpdatingRequest(
        @Size(max = 255) @NotBlank String versionName
) implements Serializable {
}