package io.github.nhatbangle.sdp.product.mapper;

import io.github.nhatbangle.sdp.product.dto.response.*;
import io.github.nhatbangle.sdp.product.entity.module.*;
import io.github.nhatbangle.sdp.product.entity.module.Module;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.Objects;

@Validated
@RequiredArgsConstructor
public class ModuleMapper implements IEntityMapper<Module, ModuleResponse> {

    private final DocumentLabelMapper mapper;

    @Override
    public @NotNull ModuleResponse toResponse(@NotNull Module entity) {
        var updatedAt = entity.getUpdatedAt();
        return new ModuleResponse(
                entity.getId(),
                entity.getIsUsed(),
                entity.getName(),
                entity.getDescription(),
                Objects.requireNonNull(entity.getCreatedAt()).toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null
        );
    }

    @NotNull
    public ModuleVersionResponse toResponse(@NotNull ModuleVersion version) {
        var updatedAt = version.getUpdatedAt();
        return new ModuleVersionResponse(
                version.getId(),
                version.getName(),
                version.getIsUsed(),
                Objects.requireNonNull(version.getCreatedAt()).toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null
        );
    }

    @NotNull
    public ModuleChangelogResponse toResponse(@NotNull ModuleChangelog changelog) {
        var updatedAt = changelog.getUpdatedAt();
        var attachmentIds = Objects.requireNonNullElse(
                        changelog.getAttachments(),
                        new HashSet<ModuleChangelogHasAttachment>())
                .stream()
                .map(obj -> new AttachmentResponse(
                        obj.getAttachment().getId(),
                        Objects.requireNonNull(obj.getCreatedAt()).toEpochMilli()))
                .sorted((a, b) -> Math.toIntExact(a.createdAtMillis() - b.createdAtMillis()))
                .toList();
        return new ModuleChangelogResponse(
                changelog.getId(),
                changelog.getTitle(),
                changelog.getDescription(),
                Objects.requireNonNull(changelog.getCreatedAt()).toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null,
                attachmentIds
        );
    }

    @NotNull
    public ModuleDocumentResponse toResponse(@NotNull ModuleDocument document) {
        var updatedAt = document.getUpdatedAt();
        var product = document.getModule();

        var labels = document.getLabels();
        var labelResponses = labels != null ? labels.stream()
                .map(obj -> mapper.toResponse(obj.getLabel()))
                .toList() : null;

        var attachments = document.getAttachments();
        var attachmentResponses = attachments != null ? attachments.parallelStream()
                .map(obj -> new AttachmentResponse(
                        obj.getAttachment().getId(),
                        Objects.requireNonNull(obj.getCreatedAt()).toEpochMilli())
                ).toList() : null;

        return new ModuleDocumentResponse(
                document.getId(),
                document.getTitle(),
                document.getDescription(),
                Objects.requireNonNull(document.getCreatedAt()).toEpochMilli(),
                updatedAt != null ? updatedAt.toEpochMilli() : null,
                product != null ? product.getId() : null,
                labelResponses,
                attachmentResponses
        );
    }

}
