package io.github.nhatbangle.sdp.product.repository.module;

import io.github.nhatbangle.sdp.product.entity.module.Module;
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
public interface ModuleRepository extends JpaRepository<Module, String> {

    @Query("""
            select m from Module m
            where m.productVersion.id = :productVersionId
                and (:name is null or upper(m.name) like upper(concat('%', :name, '%')))
                and (:isUsed is null or m.isUsed = :isUsed)
            """)
    @NotNull
    Page<Module> findAllByProductVersion_IdAndNameContainsIgnoreCaseAndIsUsed(
            @NotNull @UUID @Param("productVersionId") String productVersionId,
            @Nullable @Param("name") String moduleName,
            @Nullable @Param("isUsed") Boolean isUsed,
            @NotNull Pageable pageable
    );

}