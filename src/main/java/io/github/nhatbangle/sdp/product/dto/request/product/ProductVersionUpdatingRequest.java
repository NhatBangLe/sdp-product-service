package io.github.nhatbangle.sdp.product.dto.request.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.product.entity.product.ProductVersion}
 */
public record ProductVersionUpdatingRequest(
        @Size(max = 255) @NotBlank String versionName
) implements Serializable {
}