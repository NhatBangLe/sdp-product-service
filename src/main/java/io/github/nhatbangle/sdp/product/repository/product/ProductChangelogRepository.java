package io.github.nhatbangle.sdp.product.repository.product;

import io.github.nhatbangle.sdp.product.entity.product.ProductChangelog;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ProductChangelogRepository extends JpaRepository<ProductChangelog, String> {

    @NotNull
    @Query("""
            select p from ProductChangelog p
            where p.productVersion.id = :productVersionId
                and (:title is null or upper(p.title) like upper(concat('%', :title, '%')))
            """)
    Page<ProductChangelog> findAllByProductVersion_IdAndTitleContainsIgnoreCase(
            @NotNull @UUID @Param("productVersionId") String productVersionId,
            @Nullable @Size(max = 150) @Param("title") String changelogTitle,
            @NotNull Pageable pageable
    );

}