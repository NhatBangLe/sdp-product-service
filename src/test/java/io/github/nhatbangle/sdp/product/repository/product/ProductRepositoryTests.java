package io.github.nhatbangle.sdp.product.repository.product;

import io.github.nhatbangle.sdp.product.entity.User;
import io.github.nhatbangle.sdp.product.entity.product.Product;
import io.github.nhatbangle.sdp.product.repository.RepositoryTests;
import io.github.nhatbangle.sdp.product.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
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
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    private static final User newUser = User.builder().id(UUID.randomUUID().toString()).build();

    @Test
    void createNewProduct() {
        var name = "Test product versionName";
        var description = "Test product description";
        var savedProduct = createProduct(name, description);

        log.info("Saved product: {}", savedProduct);
        assertNotNull(savedProduct.getId());
        assertEquals(newUser.getId(), savedProduct.getUser().getId());
    }

    @Test
    void deleteProduct_without_deleteUser() {
        var userId = newUser.getId();
        var name = "Test product versionName";
        var description = "Test product description";
        var productId = createProduct(name, description).getId();

        log.info("Created product with ID: {}", productId);
        assertNotNull(productId);

        productRepository.deleteById(productId);
        log.info("Deleted product with ID: {}", productId);
        assertNull(productRepository.findById(productId).orElse(null));
        assertNotNull(userRepository.findById(userId).orElse(null));
    }

    @ParameterizedTest
    @MethodSource(value = "findAllProducts_TestSource")
    void findAllByUser_IdAndNameContainsIgnoreCaseAndIsUsed(
            int numOfInitElements,
            String userId,
            String name,
            Boolean isUsed,
            int expectedTotalElements
    ) {
        for (int i = 0; i < numOfInitElements; i++) {
            var productName = "Test product versionName " + i;
            var description = "Test product description " + i;
            createProduct(productName, description);
        }

        var products = productRepository.findAllByUser_IdAndNameContainsIgnoreCaseAndIsUsed(
                userId,
                name,
                isUsed,
                PageRequest.of(0, Math.max(expectedTotalElements, 1))
        );

        assertNotNull(products);
        assertEquals(expectedTotalElements, products.getTotalElements());
    }

    private static Stream<Arguments> findAllProducts_TestSource() {
        var unknownUserId = UUID.randomUUID().toString();
        var createdUserId = newUser.getId();

        return Stream.of(
                Arguments.of(1, unknownUserId, "pro", true, 0),
                Arguments.of(5, unknownUserId, "pro", true, 0),
                Arguments.of(10, unknownUserId, "pro", false, 0),
                Arguments.of(20, unknownUserId, "pro", null, 0),
                Arguments.of(1, unknownUserId, "", true, 0),
                Arguments.of(5, unknownUserId, "", true, 0),
                Arguments.of(10, unknownUserId, "", false, 0),
                Arguments.of(20, unknownUserId, "", null, 0),
                Arguments.of(1, unknownUserId, null, true, 0),
                Arguments.of(5, unknownUserId, null, true, 0),
                Arguments.of(10, unknownUserId, null, false, 0),
                Arguments.of(20, unknownUserId, null, null, 0),
                Arguments.of(1, unknownUserId, "Test", true, 0),
                Arguments.of(5, unknownUserId, "Test", true, 0),
                Arguments.of(10, unknownUserId, "Test", false, 0),
                Arguments.of(20, unknownUserId, "Test", null, 0),

                Arguments.of(1, createdUserId, "pro", true, 0),
                Arguments.of(5, createdUserId, "pro", true, 0),
                Arguments.of(10, createdUserId, "pro", false, 10),
                Arguments.of(20, createdUserId, "pro", null, 20),
                Arguments.of(1, createdUserId, "", true, 0),
                Arguments.of(5, createdUserId, "", true, 0),
                Arguments.of(10, createdUserId, "", false, 10),
                Arguments.of(20, createdUserId, "", null, 20),
                Arguments.of(1, createdUserId, null, true, 0),
                Arguments.of(5, createdUserId, null, true, 0),
                Arguments.of(10, createdUserId, null, false, 10),
                Arguments.of(20, createdUserId, null, null, 20),
                Arguments.of(1, createdUserId, "Test", true, 0),
                Arguments.of(5, createdUserId, "Test", true, 0),
                Arguments.of(10, createdUserId, "Test", false, 10),
                Arguments.of(20, createdUserId, "Test", null, 20)
        );
    }

    @NotNull
    private Product createProduct(String name, String description) {
        var product = Product.builder()
                .name(name)
                .description(description)
                .user(newUser)
                .build();
        return productRepository.save(product);
    }

}