package io.github.nhatbangle.sdp.product.service.product;

import io.github.nhatbangle.sdp.product.dto.request.product.ProductCreatingRequest;
import io.github.nhatbangle.sdp.product.dto.request.product.ProductUpdatingRequest;
import io.github.nhatbangle.sdp.product.entity.User;
import io.github.nhatbangle.sdp.product.entity.product.Product;
import io.github.nhatbangle.sdp.product.exception.ServiceUnavailableException;
import io.github.nhatbangle.sdp.product.repository.product.ProductRepository;
import io.github.nhatbangle.sdp.product.service.UserService;
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
import java.util.NoSuchElementException;

@Service
@Validated
@RequiredArgsConstructor
@CacheConfig(cacheNames = "products")
public class ProductService {

    private final ProductRepository productRepository;
    private final UserService userService;
    private final MessageSource messageSource;

    @NotNull
    public Page<Product> queryAllProducts(
            @NotNull @UUID String userId,
            @Nullable String name,
            @Nullable Boolean isUsed,
            @NotNull Pageable pageable
    ) {
        return productRepository.findAllByUser_IdAndNameContainsIgnoreCaseAndIsUsed(
                userId,
                name,
                isUsed,
                pageable
        );
    }

    @NotNull
    @Cacheable(key = "#productId")
    public Product getProduct(@NotNull @UUID String productId)
            throws NoSuchElementException {
        return findProduct(productId);
    }

    private Product findProduct(String productId) throws NoSuchElementException {
        return productRepository.findById(productId).orElseThrow(() -> {
            var message = messageSource.getMessage(
                    "product.not_found",
                    new Object[]{productId},
                    Locale.getDefault()
            );
            return new NoSuchElementException(message);
        });
    }

    @NotNull
    public Product createProduct(@NotNull @Valid ProductCreatingRequest request)
            throws ServiceUnavailableException {
        User user;
        var userId = request.userId();
        try {
            user = userService.getUserById(userId);
        } catch (NoSuchElementException e) {
            user = User.builder().id(userId).build();
        }

        var product = Product.builder()
                .name(request.name())
                .description(request.description())
                .user(user)
                .build();
        return productRepository.save(product);
    }

    @NotNull
    @CachePut(key = "#productId")
    public Product updateProduct(
            @NotNull @UUID String productId,
            @NotNull @Valid ProductUpdatingRequest body
    ) throws NoSuchElementException {
        var product = findProduct(productId);
        product.setName(body.name());
        product.setDescription(body.description());
        return productRepository.save(product);
    }

    @CacheEvict(key = "#productId")
    public void deleteProduct(@NotNull @UUID String productId) {
        productRepository.deleteById(productId);
    }

}
