package io.github.nhatbangle.sdp.product.repository.product;

import io.github.nhatbangle.sdp.product.entity.User;
import io.github.nhatbangle.sdp.product.entity.product.Product;
import io.github.nhatbangle.sdp.product.entity.product.ProductVersion;
import io.github.nhatbangle.sdp.product.repository.RepositoryTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class ProductVersionRepositoryTests extends RepositoryTests {

    @Autowired
    private ProductVersionRepository versionRepository;
    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setup() {
        var p = Product.builder()
                .name("Test product")
                .description("Test product description")
                .createdAt(Instant.now())
                .user(User.builder().id(UUID.randomUUID().toString()).build())
                .build();
        product = productRepository.save(p);
    }

    @Test
    void createNewVersion() {
        var versionName = "0.0.1-SNAPSHOT";
        var newVersion = ProductVersion.builder()
                .name(versionName)
                .product(product)
                .build();
        var savedVersion = versionRepository.save(newVersion);

        log.info("Saved version: {}", savedVersion);
        assertNotNull(savedVersion.getId());
        assertEquals(versionName, savedVersion.getName());
        assertEquals(product.getId(), savedVersion.getProduct().getId());
    }

}