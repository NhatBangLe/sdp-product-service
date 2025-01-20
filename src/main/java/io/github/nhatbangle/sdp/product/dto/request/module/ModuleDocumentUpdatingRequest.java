package io.github.nhatbangle.sdp.product.dto.request.module;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * DTO for {@link io.github.nhatbangle.sdp.product.entity.module.ModuleDocument}
 */
public record ModuleDocumentUpdatingRequest(
        @NotBlank @Size(max = 150) String title,
        @Nullable @Size(max = 255) String description,
        @Nullable List<String> labelIds,
        @Nullable Set<String> attachmentIds,
        @Nullable String moduleId
) implements Serializable {
}