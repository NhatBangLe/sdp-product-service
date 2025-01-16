package io.github.nhatbangle.sdp.product.repository.product;

import io.github.nhatbangle.sdp.product.entity.User;
import io.github.nhatbangle.sdp.product.entity.product.Product;
import io.github.nhatbangle.sdp.product.repository.RepositoryTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class ProductRepositoryTests extends RepositoryTests {

    @Autowired
    private ProductRepository repository;

    private final User newUser = User.builder().id(UUID.randomUUID().toString()).build();

    @Test
    void createNewProduct() {
        var name = "Test product versionName";
        var description = "Test product description";
        var product = Product.builder()
                .name(name)
                .description(description)
                .user(newUser)
                .build();
        var savedProduct = repository.save(product);

        log.info("Saved product: {}", savedProduct);
        assertNotNull(savedProduct.getId());
        assertEquals(name, savedProduct.getName());
        assertEquals(description, savedProduct.getDescription());
        assertEquals(newUser.getId(), savedProduct.getUser().getId());
    }

    @ParameterizedTest
    @MethodSource(value = "findAllProducts_TestSource")
    void findAllByUser_IdAndNameIgnoreCase(
            String name
    ) {
        var numOfProducts = 20;
        for (int i = 0; i < numOfProducts; i++) {
            var product = Product.builder()
                    .name("Test product versionName " + i)
                    .description("Test product description" + i)
                    .user(newUser)
                    .build();
            repository.save(product);
        }

        var userId = newUser.getId();
        var pageable = PageRequest.of(0, numOfProducts);

        var products = repository.findAllByUser_IdAndNameIgnoreCase(
                userId,
                name,
                pageable
        );

        assertNotNull(products);
        assertEquals(0, products.getTotalElements());
    }

    private static Stream<Arguments> findAllProducts_TestSource() {
        return Stream.of(
                Arguments.of("pro"),
                Arguments.of("")
        );
    }

}