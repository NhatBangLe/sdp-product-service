package io.github.nhatbangle.sdp.product.repository.module;

import io.github.nhatbangle.sdp.product.entity.module.Module;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

@CacheConfig(cacheNames = "module")
public interface ModuleRepository extends JpaRepository<Module, String> {

    @NotNull
    @Cacheable
    Page<Module> findAllByProductVersion_IdAndNameContainsIgnoreCaseAndIsUsed(
            @NotNull @UUID String productVersionId,
            @NotNull String name,
            boolean isUsed,
            @NotNull Pageable pageable
    );

}