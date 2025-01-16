package io.github.nhatbangle.sdp.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.product.entity.DocumentLabel}
 */
public record DocumentLabelCreatingRequest(
        @Size(max = 100) @NotBlank String name,
        @Size(max = 255) String description,
        @Size(max = 6) String color,
        @UUID @NotNull String userId
) implements Serializable {
}