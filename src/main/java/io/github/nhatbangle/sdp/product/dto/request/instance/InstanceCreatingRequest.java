package io.github.nhatbangle.sdp.product.dto.request.instance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link io.github.nhatbangle.sdp.product.entity.instance.Instance}
 */
public record InstanceCreatingRequest(
        @NotNull @Size(max = 150) @NotBlank String name,
        @Size(max = 255) String description,
        @NotNull @UUID String moduleVersionId,
        Set<InstanceAttributeRequest> attributes
) implements Serializable {
}