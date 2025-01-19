package io.github.nhatbangle.sdp.product.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.product.entity.DocumentLabel}
 */
public record DocumentLabelCreatingRequest(
        @NotBlank @Size(max = 100) String name,
        @Nullable @Size(max = 255) String description,
        @Nullable @Size(max = 6) String color,
        @NotNull @UUID String userId
) implements Serializable {
}