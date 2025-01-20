package io.github.nhatbangle.sdp.product.repository.module;

import io.github.nhatbangle.sdp.product.entity.module.ModuleDocument;
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
public interface ModuleDocumentRepository extends JpaRepository<ModuleDocument, String> {

    @NotNull
    @Query("""
            select m from ModuleDocument m
            where m.user.id = :userId
                and (:moduleName is null or upper(m.module.name) like upper(concat('%', :moduleName, '%')))
                and (:title is null or upper(m.title) like upper(concat('%', :title, '%')))
            """)
    Page<ModuleDocument> findAllByUser_IdAndModule_NameContainsIgnoreCaseAndTitleContainsIgnoreCase(
            @NotNull @UUID @Param("userId") String userId,
            @Nullable @Param("moduleName") String moduleName,
            @Nullable @Param("title") String documentTitle,
            @NotNull Pageable pageable
    );

}