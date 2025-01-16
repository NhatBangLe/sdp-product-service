package io.github.nhatbangle.sdp.product.repository.product;

import io.github.nhatbangle.sdp.product.entity.product.ProductChangelog;
import jakarta.validation.constraints.NotBlank;
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
@CacheConfig(cacheNames = "product-changelog")
public interface ProductChangelogRepository extends JpaRepository<ProductChangelog, String> {

    @NotNull
    @Cacheable
    Page<ProductChangelog> findAllByProductVersion_IdAndTitleContainsIgnoreCase(
            @NotNull @UUID String productVersionId,
            @NotBlank @Size(max = 150) String title,
            @NotNull Pageable pageable
    );

}