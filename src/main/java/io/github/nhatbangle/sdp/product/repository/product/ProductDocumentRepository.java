package io.github.nhatbangle.sdp.product.repository.product;

import io.github.nhatbangle.sdp.product.entity.product.ProductDocument;
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
public interface ProductDocumentRepository extends JpaRepository<ProductDocument, String> {

    @NotNull
    @Query("""
            select p from ProductDocument p
            where p.user.id = :userId
                and (:productName is null or upper(p.product.name) like upper(concat('%', :productName, '%')))
                and (:title is null or upper(p.title) like upper(concat('%', :title, '%')))
            """)
    Page<ProductDocument> findAllByUser_IdAndProduct_NameAndTitleContainsIgnoreCase(
            @NotNull @UUID @Param("userId") String userId,
            @Nullable @UUID @Param("productName") String productName,
            @Nullable @Param("title") String documentTitle,
            @NotNull Pageable pageable
    );

}