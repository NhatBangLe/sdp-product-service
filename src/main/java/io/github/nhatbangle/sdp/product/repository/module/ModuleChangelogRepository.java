package io.github.nhatbangle.sdp.product.repository.module;

import io.github.nhatbangle.sdp.product.entity.module.ModuleChangelog;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ModuleChangelogRepository extends JpaRepository<ModuleChangelog, String> {

    @Query("""
            select m from ModuleChangelog m
            where m.moduleVersion.id = :moduleVersionId
                and (:title is null or upper(m.title) like upper(concat('%', :title, '%')))
            """)
    @NotNull
    Page<ModuleChangelog> findAllByModuleVersion_IdAndTitleContainsIgnoreCase(
            @NotNull @UUID @Param("moduleVersionId") String moduleVersionId,
            @Nullable @Param("title") String changelogTitle,
            @NotNull Pageable pageable
    );

}