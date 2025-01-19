package io.github.nhatbangle.sdp.product.service.module;

import io.github.nhatbangle.sdp.product.dto.request.module.ModuleCreatingRequest;
import io.github.nhatbangle.sdp.product.dto.request.module.ModuleUpdatingRequest;
import io.github.nhatbangle.sdp.product.entity.module.Module;
import io.github.nhatbangle.sdp.product.exception.ServiceUnavailableException;
import io.github.nhatbangle.sdp.product.repository.module.ModuleRepository;
import io.github.nhatbangle.sdp.product.service.product.ProductVersionService;
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
@CacheConfig(cacheNames = "modules")
public class ModuleService {

    private final MessageSource messageSource;
    private final ModuleRepository moduleRepository;
    private final ProductVersionService productVersionService;

    @NotNull
    public Page<Module> queryAllModules(
            @NotNull @UUID String productVersionId,
            @Nullable String moduleName,
            boolean isUsed,
            @NotNull Pageable pageable
    ) {
        return moduleRepository.findAllByProductVersion_IdAndNameContainsIgnoreCaseAndIsUsed(
                productVersionId,
                moduleName,
                isUsed,
                pageable
        );
    }

    @NotNull
    @Cacheable(key = "#moduleId")
    public Module getModule(@NotNull @UUID String moduleId) throws IllegalArgumentException {
        return findModule(moduleId);
    }

    private Module findModule(String moduleId)
            throws IllegalArgumentException {
        return moduleRepository.findById(moduleId).orElseThrow(() -> {
            var message = messageSource.getMessage(
                    "module.not_found",
                    new Object[]{moduleId},
                    Locale.getDefault()
            );
            return new IllegalArgumentException(message);
        });
    }

    @NotNull
    public Module createModule(@NotNull @Valid ModuleCreatingRequest request)
            throws IllegalArgumentException, ServiceUnavailableException {
        var productVersion = productVersionService.getVersion(request.productVersionId());
        var module = Module.builder()
                .name(request.name())
                .description(request.description())
                .productVersion(productVersion)
                .build();
        return moduleRepository.save(module);
    }


    @NotNull
    @CachePut(key = "#moduleId")
    public Module updateModule(
            @NotNull @UUID String moduleId,
            @NotNull @Valid ModuleUpdatingRequest body
    ) {
        var module = findModule(moduleId);
        module.setName(body.name());
        module.setDescription(body.description());
        return moduleRepository.save(module);
    }

    @CacheEvict(key = "#moduleId")
    public void deleteModule(@NotNull @UUID String moduleId) {
        moduleRepository.deleteById(moduleId);
    }

}
