package io.github.nhatbangle.sdp.product.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.product.entity.DocumentLabel}
 */
public record DocumentLabelUpdatingRequest(
        @NotBlank @Size(max = 100) String name,
        @Nullable @Size(max = 255) String description,
        @NotNull @Size(max = 6) String color
) implements Serializable {
}