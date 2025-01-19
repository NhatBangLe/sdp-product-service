package io.github.nhatbangle.sdp.product.service.product;

import io.github.nhatbangle.sdp.product.dto.request.product.ProductVersionCreatingRequest;
import io.github.nhatbangle.sdp.product.dto.request.product.ProductVersionUpdatingRequest;
import io.github.nhatbangle.sdp.product.entity.product.ProductVersion;
import io.github.nhatbangle.sdp.product.repository.product.ProductVersionRepository;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Locale;

@Service
@Validated
@RequiredArgsConstructor
@CacheConfig(cacheNames = "product-versions")
public class ProductVersionService {

    private final MessageSource messageSource;
    private final ProductVersionRepository versionRepository;
    private final ProductService productService;

    @NotNull
    public Page<ProductVersion> queryAllVersions(
            @NotNull @UUID String productId,
            @Nullable String name,
            @Nullable Boolean isUsed,
            @NotNull Pageable pageable
    ) {
        return versionRepository.findAllByProduct_IdAndNameContainsIgnoreCaseAndIsUsed(
                productId,
                name,
                isUsed,
                pageable
        );
    }

    @NotNull
    @Cacheable(key = "#versionId")
    public ProductVersion getVersion(
            @NotNull @UUID String versionId
    ) throws IllegalArgumentException {
        return findVersion(versionId);
    }

    private ProductVersion findVersion(
            String versionId
    ) throws IllegalArgumentException {
        return versionRepository.findById(versionId).orElseThrow(() -> {
            var message = messageSource.getMessage(
                    "product_version.not_found",
                    new Object[]{versionId},
                    Locale.getDefault()
            );
            return new IllegalArgumentException(message);
        });
    }

    @NotNull
    public ProductVersion createVersion(
            @NotNull @UUID String productId,
            @NotNull @Valid ProductVersionCreatingRequest request
    ) throws IllegalArgumentException {
        var product = productService.getProduct(productId);
        var newVersion = ProductVersion.builder()
                .name(request.versionName())
                .product(product)
                .build();
        return versionRepository.save(newVersion);
    }

    @NotNull
    @CachePut(key = "#versionId")
    public ProductVersion updateVersion(
            @NotNull @UUID String versionId,
            @NotNull @Valid ProductVersionUpdatingRequest request
    ) throws IllegalArgumentException {
        var version = findVersion(versionId);
        version.setName(request.versionName());
        return versionRepository.save(version);
    }

    @CacheEvict(key = "#versionId")
    public void deleteVersion(@NotNull @UUID String versionId) {
        versionRepository.deleteById(versionId);
    }

}
