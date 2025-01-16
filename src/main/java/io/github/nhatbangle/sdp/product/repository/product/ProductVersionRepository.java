package io.github.nhatbangle.sdp.product.repository.product;

import io.github.nhatbangle.sdp.product.entity.product.ProductVersion;
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
@CacheConfig(cacheNames = "product-version")
public interface ProductVersionRepository extends JpaRepository<ProductVersion, String> {

    @NotNull
    @Cacheable
    Page<ProductVersion> findAllByProduct_IdAndNameContainsIgnoreCase(
            @NotNull @UUID String productId,
            @NotNull @Size(max = 255) String name,
            @NotNull Pageable pageable
    );

}