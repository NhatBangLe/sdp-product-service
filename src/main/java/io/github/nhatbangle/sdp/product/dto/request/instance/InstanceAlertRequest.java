package io.github.nhatbangle.sdp.product.dto.request.instance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;

public record InstanceAlertRequest(
        @NotNull @UUID String instanceId,
        @NotBlank @Size(max = 80) String secretKey
) {
}
