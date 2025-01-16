package io.github.nhatbangle.sdp.product.repository;

import io.github.nhatbangle.sdp.product.entity.module.ModuleChangelog;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

@Validated
@CacheConfig(cacheNames = "module-changelog")
public interface ModuleChangelogRepository extends JpaRepository<ModuleChangelog, String> {

    @NotNull
    @Cacheable
    Page<ModuleChangelog> findAllByModuleVersion_IdAndTitleContainsIgnoreCase(
            @NotNull @UUID String moduleVersionId,
            @NotNull String title,
            @NotNull Pageable pageable
    );

}