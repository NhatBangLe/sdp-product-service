package io.github.nhatbangle.sdp.product.repository.product;

import io.github.nhatbangle.sdp.product.entity.product.Product;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

@Validated
@CacheConfig(cacheNames = "product")
public interface ProductRepository extends JpaRepository<Product, String> {

    @NotNull
    @Cacheable
    Page<Product> findAllByUser_IdAndNameIgnoreCase(
            @NotNull @UUID String userId,
            @NotNull @Size(max = 150) String name,
            @NotNull Pageable pageable
    );

}