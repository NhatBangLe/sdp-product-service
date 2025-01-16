package io.github.nhatbangle.sdp.product.repository;

import io.github.nhatbangle.sdp.product.entity.DocumentLabel;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

@Validated
@CacheConfig(cacheNames = "document-label")
public interface DocumentLabelRepository extends JpaRepository<DocumentLabel, String> {

    @Cacheable
    Page<DocumentLabel> findAllByUser_IdAndNameContainsIgnoreCase(
            @NotNull @UUID String userId,
            @NotNull String labelName,
            @NotNull Pageable pageable
    );

}