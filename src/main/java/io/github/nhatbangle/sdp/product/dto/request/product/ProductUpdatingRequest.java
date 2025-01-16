package io.github.nhatbangle.sdp.product.dto.request.product;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.product.entity.product.Product}
 */
public record ProductUpdatingRequest(
        @Size(max = 150) @NotBlank String name,
        @Nullable String description
) implements Serializable {
}