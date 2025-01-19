package io.github.nhatbangle.sdp.product.repository.module;

import io.github.nhatbangle.sdp.product.entity.module.ModuleVersion;
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
public interface ModuleVersionRepository extends JpaRepository<ModuleVersion, String> {

    @Query("""
            select m from ModuleVersion m
            where m.module.id = :moduleId
                and (:name is null or upper(m.name) like upper(concat('%', :name, '%')))
                and (:isUsed is null or m.isUsed = :isUsed)
            """)
    @NotNull
    Page<ModuleVersion> findAllByModule_IdAndNameContainsIgnoreCaseAndIsUsed(
            @NotNull @UUID @Param("moduleId") String moduleId,
            @Nullable @Param("name") String versionName,
            @Nullable @Param("isUsed") Boolean isUsed,
            @NotNull Pageable pageable
    );

}