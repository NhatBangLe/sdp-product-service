package io.github.nhatbangle.sdp.product.dto.response;

import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link io.github.nhatbangle.sdp.product.entity.instance.Instance}
 */
public record InstanceResponse(
        String id,
        boolean isUsed,
        String name,
        @Nullable String description,
        long createdAtMillis,
        @Nullable Long updatedAtMillis,
        @Nullable List<InstanceAttributeResponse> attributes
) implements Serializable {
}