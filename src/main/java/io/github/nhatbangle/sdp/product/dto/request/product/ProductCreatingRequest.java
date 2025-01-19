package io.github.nhatbangle.sdp.product.dto.request.product;

import io.github.nhatbangle.sdp.product.entity.product.Product;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;

import java.io.Serializable;

/**
 * DTO for {@link Product}
 */
public record ProductCreatingRequest(
        @Size(max = 150) @NotBlank String name,
        @Nullable String description,
        @UUID @NotNull String userId
) implements Serializable {
}