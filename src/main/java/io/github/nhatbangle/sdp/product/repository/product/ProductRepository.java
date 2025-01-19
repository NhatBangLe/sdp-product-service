package io.github.nhatbangle.sdp.product.repository.product;

import io.github.nhatbangle.sdp.product.entity.product.Product;
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
public interface ProductRepository extends JpaRepository<Product, String> {

    @NotNull
    @Query("""
            select p from Product p
            where p.user.id = :userId
                and (:name is null or upper(p.name) like upper(concat('%', :name, '%')))
                and (:isUsed is null or p.isUsed = :isUsed)
            """)
    Page<Product> findAllByUser_IdAndNameContainsIgnoreCaseAndIsUsed(
            @NotNull @UUID @Param("userId") String userId,
            @Nullable @Size(max = 150) @Param("name") String productName,
            @Nullable @Param("isUsed") Boolean isUsed,
            @NotNull Pageable pageable
    );

}