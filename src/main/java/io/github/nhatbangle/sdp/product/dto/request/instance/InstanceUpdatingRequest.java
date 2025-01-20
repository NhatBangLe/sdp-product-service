package io.github.nhatbangle.sdp.product.dto.request.instance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.product.entity.instance.Instance}
 */
public record InstanceUpdatingRequest(
        @NotNull @Size(max = 150) @NotBlank String name,
        @Size(max = 255) String description
) implements Serializable {
}