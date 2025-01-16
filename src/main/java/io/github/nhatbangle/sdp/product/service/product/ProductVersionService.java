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
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Locale;
import java.util.Objects;

@Service
@Validated
@RequiredArgsConstructor
public class ProductVersionService {

    private final MessageSource messageSource;
    private final ProductVersionRepository versionRepository;
    private final ProductService productService;

    @NotNull
    public Page<ProductVersion> queryAllVersions(
            @NotNull @UUID String productId,
            @Nullable String name,
            @NotNull Pageable pageable
    ) {
        return versionRepository.findAllByProduct_IdAndNameContainsIgnoreCase(
                productId,
                Objects.requireNonNullElse(name, ""),
                pageable
        );
    }

    @NotNull
    public ProductVersion getVersion(
            @NotNull @UUID String versionId
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

    public void updateVersion(
            @NotNull @UUID String versionId,
            @NotNull @Valid ProductVersionUpdatingRequest request
    ) {
        var version = getVersion(versionId);
        version.setName(request.versionName());
        versionRepository.save(version);
    }

    public void deleteVersion(
            @NotNull @UUID String versionId
    ) throws IllegalArgumentException {
        var version = getVersion(versionId);
        versionRepository.delete(version);
    }

}
