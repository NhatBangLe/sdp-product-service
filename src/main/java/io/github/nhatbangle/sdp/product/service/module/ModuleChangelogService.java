package io.github.nhatbangle.sdp.product.service.module;

import io.github.nhatbangle.sdp.product.dto.request.module.ModuleChangelogCreatingRequest;
import io.github.nhatbangle.sdp.product.dto.request.module.ModuleChangelogUpdatingRequest;
import io.github.nhatbangle.sdp.product.entity.Attachment;
import io.github.nhatbangle.sdp.product.entity.id.ModuleChangelogHasAttachmentId;
import io.github.nhatbangle.sdp.product.entity.module.ModuleChangelog;
import io.github.nhatbangle.sdp.product.entity.module.ModuleChangelogHasAttachment;
import io.github.nhatbangle.sdp.product.exception.DataConflictException;
import io.github.nhatbangle.sdp.product.repository.module.ModuleChangelogRepository;
import io.github.nhatbangle.sdp.product.service.AttachmentService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
public class ModuleChangelogService {

    private final MessageSource messageSource;
    private final ModuleChangelogRepository changelogRepository;
    private final ModuleVersionService moduleVersionService;
    private final AttachmentService attachmentService;

    @NotNull
    public Page<ModuleChangelog> queryAllChangelogs(
            @NotNull @UUID String moduleVersionId,
            @Nullable String changelogTitle,
            @NotNull Pageable pageable
    ) {
        return changelogRepository.findAllByModuleVersion_IdAndTitleContainsIgnoreCase(
                moduleVersionId,
                changelogTitle,
                pageable
        );
    }

    @NotNull
    public ModuleChangelog getChangelog(@NotNull @UUID String changelogId)
            throws IllegalArgumentException {
        return changelogRepository.findById(changelogId).orElseThrow(() -> {
            var message = messageSource.getMessage(
                    "module_changelog.not_found",
                    new Object[]{changelogId},
                    Locale.getDefault()
            );
            return new IllegalArgumentException(message);
        });
    }

    @NotNull
    public ModuleChangelog createChangelog(
            @NotNull @UUID String moduleVersionId,
            @NotNull @Valid ModuleChangelogCreatingRequest request
    ) throws IllegalArgumentException {
        var moduleVersion = moduleVersionService.getVersion(moduleVersionId);
        var changelog = changelogRepository.save(ModuleChangelog.builder()
                .title(request.title())
                .description(request.description())
                .moduleVersion(moduleVersion)
                .build());

        var attachmentIds = request.attachmentIds();
        if (attachmentIds != null) {
            changelog.setAttachments(convertIdToChangelogAttachment(changelog, attachmentIds));
            return changelogRepository.save(changelog);
        }

        return changelog;
    }

    public void updateChangelog(
            @NotNull @UUID String changelogId,
            @NotNull @Valid ModuleChangelogUpdatingRequest request
    ) {
        var changelog = getChangelog(changelogId);
        changelog.setDescription(request.description());
        changelog.setTitle(request.title());
        var attachmentIds = request.attachmentIds();
        if (attachmentIds != null)
            changelog.setAttachments(convertIdToChangelogAttachment(changelog, attachmentIds));
        changelogRepository.save(changelog);
    }

    public void deleteChangelog(@NotNull @UUID String changelogId) {
        changelogRepository.deleteById(changelogId);
    }

    private Set<ModuleChangelogHasAttachment> convertIdToChangelogAttachment(
            @NotNull ModuleChangelog changelog,
            @NotNull Set<String> attachmentIds
    ) throws DataConflictException {
        // validate attachment ids
        attachmentService.validateIds(attachmentIds);

        var changelogId = changelog.getId();
        if (changelogId == null) {
            var message = messageSource.getMessage(
                    "module_changelog.not_saved",
                    null,
                    Locale.getDefault()
            );
            throw new DataConflictException(message);
        }

        return attachmentIds.stream().map(attachmentId -> {
                    var id = ModuleChangelogHasAttachmentId.builder()
                            .attachmentId(attachmentId)
                            .changelogId(changelogId)
                            .build();
                    var attachment = Attachment.builder().id(attachmentId).build();

                    return ModuleChangelogHasAttachment.builder()
                            .id(id)
                            .changelog(changelog)
                            .attachment(attachment)
                            .build();
                }
        ).collect(Collectors.toSet());
    }

}
