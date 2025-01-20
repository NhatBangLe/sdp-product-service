package io.github.nhatbangle.sdp.product.dto.request.instance;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.product.entity.instance.InstanceAttribute}
 */
public record InstanceAttributeRequest(
        @NotNull @Min(0) Short numOrder,
        @Size(max = 150) @NotBlank String key,
        @NotNull @Size(max = 255) String value
) implements Serializable {
}