package io.github.nhatbangle.sdp.product.dto.response;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.product.entity.instance.InstanceAttribute}
 */
public record InstanceAttributeResponse(
        String id,
        short numOrder,
        String key,
        String value
) implements Serializable {
}