package io.github.nhatbangle.sdp.product.repository.product;

import io.github.nhatbangle.sdp.product.entity.product.ProductVersion;
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
public interface ProductVersionRepository extends JpaRepository<ProductVersion, String> {

    @NotNull
    @Query("""
            select p from ProductVersion p
            where p.product.id = :productId
                and (:name is null or upper(p.name) like upper(concat('%', :name, '%')))
                and (:isUsed is null or p.isUsed = :isUsed)
            """)
    Page<ProductVersion> findAllByProduct_IdAndNameContainsIgnoreCaseAndIsUsed(
            @NotNull @UUID @Param("productId") String productId,
            @Nullable @Size(max = 255) @Param("name") String versionName,
            @Nullable @Param("isUsed") Boolean isUsed,
            @NotNull Pageable pageable
    );

}