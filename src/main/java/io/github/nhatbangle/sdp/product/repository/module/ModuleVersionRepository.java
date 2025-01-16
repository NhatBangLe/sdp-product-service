package io.github.nhatbangle.sdp.product.repository.module;

import io.github.nhatbangle.sdp.product.entity.module.ModuleVersion;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

@CacheConfig(cacheNames = "module-version")
public interface ModuleVersionRepository extends JpaRepository<ModuleVersion, String> {

    @NotNull
    @Cacheable
    Page<ModuleVersion> findAllByModule_IdAndNameContainsIgnoreCase(
            @NotNull @UUID String id,
            @NotNull String name,
            @NotNull Pageable pageable
    );

}