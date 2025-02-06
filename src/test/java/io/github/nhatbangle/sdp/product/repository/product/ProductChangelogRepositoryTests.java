package io.github.nhatbangle.sdp.product.repository.product;

import io.github.nhatbangle.sdp.product.entity.Attachment;
import io.github.nhatbangle.sdp.product.entity.User;
import io.github.nhatbangle.sdp.product.entity.id.ProductChangelogHasAttachmentId;
import io.github.nhatbangle.sdp.product.entity.product.*;
import io.github.nhatbangle.sdp.product.repository.RepositoryTests;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ProductChangelogRepositoryTests extends RepositoryTests {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductVersionRepository versionRepository;
    @Autowired
    private ProductChangelogRepository changelogRepository;

    private static final String userId = UUID.randomUUID().toString();
    private final User user = User.builder().id(userId).build();

    @Test
    void createNewChangelog_with_Attachments() {
        var product = createProduct("Test new product");
        assertNotNull(product.getId());

        var version = createVersion(product);
        var versionId = version.getId();
        assertNotNull(versionId);

        String title = "Changelog title", description = "Changelog description";
        var changelog = createChangelog(version, title, description);
        assertNotNull(changelog.getId());
        assertEquals(version, changelog.getProductVersion());

        int numOfAttachments = 10;
        changelog.setAttachments(createAttachments(changelog, numOfAttachments));
        var savedChangelog = changelogRepository.save(changelog);

        var attachments = savedChangelog.getAttachments();
        assertNotNull(attachments);
        assertEquals(numOfAttachments, attachments.size());
    }

    @Test
    void deleteChangelog_without_deleteProductVersion() {
        var product = createProduct("Test new product");
        assertNotNull(product.getId());

        var version = createVersion(product);
        var versionId = version.getId();
        assertNotNull(versionId);

        String title = "Changelog title", description = "Changelog description";
        var changelog = createChangelog(version, title, description);
        var changelogId = changelog.getId();
        assertNotNull(changelogId);
        assertEquals(version, changelog.getProductVersion());

        changelogRepository.delete(changelog);
        assertNotNull(versionRepository.findById(versionId).orElse(null));
        assertNull(changelogRepository.findById(changelogId).orElse(null));
    }

    @ParameterizedTest
    @MethodSource(value = "findAllChangelogs_TestSource")
    void findAllByProductVersion_IdAndTitleContainsIgnoreCase(
            int totalChangelogs,
            boolean isUnknownVersion,
            String changelogTitle,
            int expectedNumOfElements
    ) {
        var product = createProduct("Test product");
        assertNotNull(product.getId());

        var version = createVersion(product);
        var versionId = version.getId();
        assertNotNull(versionId);

        int numOfAttachments = 10;
        for (int i = 0; i < totalChangelogs; i++) {
            String title = "Changelog title", description = "Changelog description";
            var changelog = createChangelog(version, title, description);
            assertNotNull(changelog.getId());
            assertEquals(version, changelog.getProductVersion());

            changelog.setAttachments(createAttachments(changelog, numOfAttachments));
            changelogRepository.save(changelog);
        }

        var changelogs = changelogRepository.findAllByProductVersion_IdAndTitleContainsIgnoreCase(
                isUnknownVersion ? UUID.randomUUID().toString() : versionId,
                changelogTitle,
                PageRequest.of(0, Math.max(expectedNumOfElements, 1))
        );

        assertNotNull(changelogs);
        assertEquals(expectedNumOfElements, changelogs.getTotalElements());

        changelogs.forEach(changelog -> {
            var attachments = changelog.getAttachments();
            assertNotNull(attachments);
            assertEquals(numOfAttachments, attachments.size());
        });
    }

    private static Stream<Arguments> findAllChangelogs_TestSource() {
        return Stream.of(
                Arguments.of(1, true, null, 0),
                Arguments.of(1, true, "test", 0),
                Arguments.of(1, true, "pro", 0),
                Arguments.of(1, true, null, 0),
                Arguments.of(1, true, null, 0),
                Arguments.of(1, true, "test", 0),
                Arguments.of(1, true, "test", 0),
                Arguments.of(1, true, "pro", 0),
                Arguments.of(1, true, "pro", 0),

                Arguments.of(5, false, "not in", 0),
                Arguments.of(5, false, "test", 5),
                Arguments.of(5, false, null, 5),
                Arguments.of(5, false, "pro", 5)
        );
    }

    private ProductChangelog createChangelog(
            @NotNull ProductVersion version,
            @NotNull String title,
            @NotNull String description
    ) {
        return changelogRepository.save(ProductChangelog.builder()
                .title(title)
                .description(description)
                .productVersion(version)
                .build());
    }

    private ProductVersion createVersion(@NotNull Product product) {
        return versionRepository.save(ProductVersion.builder()
                .name("0.0.1-SNAPSHOT")
                .product(product)
                .build());
    }

    private Product createProduct(@NotNull String productName) {
        return productRepository.save(Product.builder()
                .name(productName)
                .description("Test product description")
                .user(user)
                .build());
    }

    private Set<ProductChangelogHasAttachment> createAttachments(
            @NotNull ProductChangelog changelog,
            int total
    ) {
        var changelogId = changelog.getId();
        assertNotNull(changelogId);

        var usedIds = new HashSet<String>(total);
        var set = new HashSet<ProductChangelogHasAttachment>(total);
        for (int i = 0; i < total; i++) {
            String attachmentId;
            do {
                attachmentId = UUID.randomUUID().toString();
            } while (usedIds.contains(attachmentId));
            usedIds.add(attachmentId);

            var attachment = ProductChangelogHasAttachment.builder()
                    .id(ProductChangelogHasAttachmentId.builder()
                            .attachmentId(attachmentId)
                            .changelogId(changelogId)
                            .build())
                    .attachment(Attachment.builder().id(attachmentId).build())
                    .changelog(changelog)
                    .build();
            set.add(attachment);
        }
        return set;
    }

}