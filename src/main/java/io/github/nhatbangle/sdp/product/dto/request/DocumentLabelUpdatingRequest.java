package io.github.nhatbangle.sdp.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.product.entity.DocumentLabel}
 */
public record DocumentLabelUpdatingRequest(
        @Size(max = 100) @NotBlank String name,
        @Size(max = 255) String description,
        @Size(max = 6) String color
) implements Serializable {
}