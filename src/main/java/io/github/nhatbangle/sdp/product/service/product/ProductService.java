package io.github.nhatbangle.sdp.product.service.product;

import io.github.nhatbangle.sdp.product.dto.request.product.ProductCreatingRequest;
import io.github.nhatbangle.sdp.product.dto.request.product.ProductUpdatingRequest;
import io.github.nhatbangle.sdp.product.entity.product.Product;
import io.github.nhatbangle.sdp.product.exception.ServiceUnavailableException;
import io.github.nhatbangle.sdp.product.repository.product.ProductRepository;
import io.github.nhatbangle.sdp.product.service.UserService;
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
public class ProductService {

    private final ProductRepository productRepository;
    private final UserService userService;
    private final MessageSource messageSource;

    @NotNull
    public Page<Product> queryAllProducts(
            @NotNull @UUID String userId,
            @Nullable String name,
            @NotNull Pageable pageable
    ) {
        return productRepository.findAllByUser_IdAndNameIgnoreCase(
                userId,
                Objects.requireNonNullElse(name, ""),
                pageable
        );
    }

    @NotNull
    public Product getProduct(@NotNull @UUID String productId)
            throws IllegalArgumentException {
        return productRepository.findById(productId).orElseThrow(() -> {
            var message = messageSource.getMessage(
                    "product.not_found",
                    new Object[]{productId},
                    Locale.getDefault()
            );
            return new IllegalArgumentException(message);
        });
    }

    @NotNull
    public Product createProduct(@NotNull @Valid ProductCreatingRequest request)
            throws IllegalArgumentException, ServiceUnavailableException {
        var user = userService.getUserById(request.userId());
        var product = Product.builder()
                .name(request.name())
                .description(request.description())
                .user(user)
                .build();
        return productRepository.save(product);
    }

    public void updateProduct(
            @NotNull @UUID String productId,
            @NotNull @Valid ProductUpdatingRequest body
    ) {
        var product = getProduct(productId);
        product.setName(body.name());
        product.setDescription(body.description());
        productRepository.save(product);
    }

    public void deleteProduct(@NotNull @UUID String productId) {
        var product = getProduct(productId);
        productRepository.delete(product);
    }

}
