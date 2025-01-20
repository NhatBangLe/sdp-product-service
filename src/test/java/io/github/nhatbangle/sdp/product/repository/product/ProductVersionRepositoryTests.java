package io.github.nhatbangle.sdp.product.repository.product;

import io.github.nhatbangle.sdp.product.entity.User;
import io.github.nhatbangle.sdp.product.entity.product.Product;
import io.github.nhatbangle.sdp.product.entity.product.ProductVersion;
import io.github.nhatbangle.sdp.product.repository.RepositoryTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class ProductVersionRepositoryTests extends RepositoryTests {

    @Autowired
    private ProductVersionRepository versionRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    void createNewVersion() {
        var product = createProduct();
        assertNotNull(product.getId());

        var versionName = "0.0.1-SNAPSHOT";
        var savedVersion = createVersion(product, versionName);

        log.info("Saved version: {}", savedVersion);
        assertNotNull(savedVersion.getId());
        assertEquals(product.getId(), savedVersion.getProduct().getId());
    }

    @Test
    void deleteVersion_without_deleteProduct() {
        var product = createProduct();
        assertNotNull(product.getId());

        var versionName = "0.0.1-SNAPSHOT";
        var savedVersionId = createVersion(product, versionName).getId();
        assertNotNull(savedVersionId);

        versionRepository.deleteById(savedVersionId);

        log.info("Deleted product version ID: {}", savedVersionId);
        assertNull(versionRepository.findById(savedVersionId).orElse(null));
        assertNotNull(productRepository.findById(product.getId()).orElse(null));
    }

    @ParameterizedTest
    @MethodSource(value = "findAllVersions_TestSource")
    void findAllByProduct_IdAndNameContainsIgnoreCaseAndIsUsed(
            int numOfInitialElements,
            boolean isUnknownProduct,
            String versionName,
            Boolean isUsed,
            int expectedTotalElements
    ) {
        var product = createProduct();
        assertNotNull(product.getId());

        for (int i = 0; i < numOfInitialElements; i++) {
            var name = "Product version " + i;
            createVersion(product, name);
        }

        var productId = isUnknownProduct ? UUID.randomUUID().toString() : product.getId();
        var versions = versionRepository.findAllByProduct_IdAndNameContainsIgnoreCaseAndIsUsed(
                productId,
                versionName,
                isUsed,
                PageRequest.of(0, Math.max(expectedTotalElements, 1))
        );

        assertNotNull(versions);
        assertEquals(expectedTotalElements, versions.getTotalElements());
    }

    private static Stream<Arguments> findAllVersions_TestSource() {
        return Stream.of(
                Arguments.of(1, true, "", true, 0),
                Arguments.of(1, true, "", false, 0),
                Arguments.of(1, true, "", null, 0),
                Arguments.of(1, true, "Pro", true, 0),
                Arguments.of(1, true, "Pro", false, 0),
                Arguments.of(1, true, "Pro", null, 0),
                Arguments.of(1, true, "ver", true, 0),
                Arguments.of(1, true, "ver", false, 0),
                Arguments.of(1, true, "ver", null, 0),
                Arguments.of(0, true, "Test", true, 0),
                Arguments.of(0, true, "Test", false, 0),
                Arguments.of(5, true, "Test", null, 0),
                Arguments.of(1, true, null, true, 0),
                Arguments.of(1, true, null, false, 0),
                Arguments.of(1, true, null, null, 0),

                Arguments.of(0, false, "", true, 0),
                Arguments.of(0, false, "", false, 0),
                Arguments.of(5, false, "", null, 5),
                Arguments.of(0, false, "Pro", true, 0),
                Arguments.of(0, false, "Pro", false, 0),
                Arguments.of(5, false, "Pro", null, 5),
                Arguments.of(0, false, "ver", true, 0),
                Arguments.of(0, false, "ver", false, 0),
                Arguments.of(5, false, "ver", null, 5),
                Arguments.of(0, false, "Test", true, 0),
                Arguments.of(0, false, "Test", false, 0),
                Arguments.of(5, false, "Test", null, 0),
                Arguments.of(0, false, null, true, 0),
                Arguments.of(0, false, null, false, 0),
                Arguments.of(5, false, null, null, 5)
        );
    }

    private Product createProduct() {
        return productRepository.save(Product.builder()
                .name("Test product")
                .description("Test product description")
                .createdAt(Instant.now())
                .user(User.builder().id(UUID.randomUUID().toString()).build())
                .build());
    }

    private ProductVersion createVersion(Product product, String versionName) {
        var newVersion = ProductVersion.builder()
                .name(versionName)
                .product(product)
                .build();
        return versionRepository.save(newVersion);
    }

}