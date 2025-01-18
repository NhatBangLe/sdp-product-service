package io.github.nhatbangle.sdp.product.service.module;

import io.github.nhatbangle.sdp.product.dto.request.module.ModuleVersionCreatingRequest;
import io.github.nhatbangle.sdp.product.dto.request.module.ModuleVersionUpdatingRequest;
import io.github.nhatbangle.sdp.product.entity.module.ModuleVersion;
import io.github.nhatbangle.sdp.product.repository.module.ModuleVersionRepository;
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

import java.util.Locale;
import java.util.Objects;

@Service
@Validated
@RequiredArgsConstructor
public class ModuleVersionService {

    private final MessageSource messageSource;
    private final ModuleVersionRepository versionRepository;
    private final ModuleService moduleService;

    @NotNull
    public Page<ModuleVersion> queryAllVersions(
            @NotNull @UUID String moduleId,
            @Nullable String name,
            @NotNull Pageable pageable
    ) {
        return versionRepository.findAllByModule_IdAndNameContainsIgnoreCase(
                moduleId,
                Objects.requireNonNullElse(name, ""),
                pageable
        );
    }

    @NotNull
    public ModuleVersion getVersion(
            @NotNull @UUID String versionId
    ) throws IllegalArgumentException {
        return versionRepository.findById(versionId).orElseThrow(() -> {
            var message = messageSource.getMessage(
                    "module_version.not_found",
                    new Object[]{versionId},
                    Locale.getDefault()
            );
            return new IllegalArgumentException(message);
        });
    }

    @NotNull
    public ModuleVersion createVersion(
            @NotNull @UUID String productId,
            @NotNull @Valid ModuleVersionCreatingRequest request
    ) throws IllegalArgumentException {
        var module = moduleService.getModule(productId);
        var newVersion = ModuleVersion.builder()
                .name(request.versionName())
                .module(module)
                .build();
        return versionRepository.save(newVersion);
    }

    public void updateVersion(
            @NotNull @UUID String versionId,
            @NotNull @Valid ModuleVersionUpdatingRequest request
    ) {
        var version = getVersion(versionId);
        version.setName(request.versionName());
        versionRepository.save(version);
    }

    public void deleteVersion(@NotNull @UUID String versionId) {
        versionRepository.deleteById(versionId);
    }

}
