package io.github.nhatbangle.sdp.product.mapper;

import io.github.nhatbangle.sdp.product.dto.response.InstanceAttributeResponse;
import io.github.nhatbangle.sdp.product.dto.response.InstanceResponse;
import io.github.nhatbangle.sdp.product.entity.instance.Instance;
import io.github.nhatbangle.sdp.product.entity.instance.InstanceAttribute;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

@Validated
public class InstanceMapper implements IEntityMapper<Instance, InstanceResponse> {

    @Override
    public @NotNull InstanceResponse toResponse(@NotNull Instance entity) {
        var updatedAt = entity.getUpdatedAt();
        var attributes = entity.getAttributes();
        return new InstanceResponse(
                entity.getId(),
                entity.getIsUsed(),
                entity.getName(),
                entity.getDescription(),
                Objects.requireNonNull(entity.getCreatedAt()).toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null,
                attributes != null ? attributes.parallelStream().map(this::toResponse).toList() : null
        );
    }

    @NotNull
    public InstanceAttributeResponse toResponse(InstanceAttribute attribute) {
        return new InstanceAttributeResponse(
                attribute.getId(),
                attribute.getNumOrder(),
                attribute.getKey(),
                attribute.getValue()
        );
    }

}
