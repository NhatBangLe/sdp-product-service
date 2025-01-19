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
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Locale;

@Service
@Validated
@RequiredArgsConstructor
@CacheConfig(cacheNames = "module-versions")
public class ModuleVersionService {

    private final MessageSource messageSource;
    private final ModuleVersionRepository versionRepository;
    private final ModuleService moduleService;

    @NotNull
    public Page<ModuleVersion> queryAllVersions(
            @NotNull @UUID String moduleId,
            @Nullable String name,
            @Nullable Boolean isUsed,
            @NotNull Pageable pageable
    ) {
        return versionRepository.findAllByModule_IdAndNameContainsIgnoreCaseAndIsUsed(
                moduleId,
                name,
                isUsed,
                pageable
        );
    }

    @NotNull
    @Cacheable(key = "#versionId")
    public ModuleVersion getVersion(@NotNull @UUID String versionId) throws IllegalArgumentException {
        return findVersion(versionId);
    }

    private ModuleVersion findVersion(String versionId) throws IllegalArgumentException {
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

    @NotNull
    @CachePut(key = "#versionId")
    public ModuleVersion updateVersion(
            @NotNull @UUID String versionId,
            @NotNull @Valid ModuleVersionUpdatingRequest request
    ) {
        var version = findVersion(versionId);
        version.setName(request.versionName());
        return versionRepository.save(version);
    }

    @CacheEvict(key = "#versionId")
    public void deleteVersion(@NotNull @UUID String versionId) {
        versionRepository.deleteById(versionId);
    }

}
