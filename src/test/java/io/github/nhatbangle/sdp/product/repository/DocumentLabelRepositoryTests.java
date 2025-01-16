package io.github.nhatbangle.sdp.product.repository;

import io.github.nhatbangle.sdp.product.entity.DocumentLabel;
import io.github.nhatbangle.sdp.product.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class DocumentLabelRepositoryTests extends RepositoryTests {

    @Autowired
    private DocumentLabelRepository repository;

    private final User newUser = User.builder().id(UUID.randomUUID().toString()).build();

    @Test
    public void createLabel_with_NewUser() {
        var label = DocumentLabel.builder()
                .name("Test")
                .description("Test description")
                .user(newUser)
                .build();

        var savedLabel = repository.save(label);
        log.info("Saved label: {}", savedLabel);

        assertNotNull(savedLabel.getId());
        assertEquals("Test", savedLabel.getName());
        assertEquals("Test description", savedLabel.getDescription());
        assertEquals(newUser.getId(), savedLabel.getUser().getId());
    }

    @Test
    public void findAllLabels_By_UserId_And_NameInMiddle() {
        var numOfLabels = 20;
        for (int i = 0; i < numOfLabels; i++) {
            var label = DocumentLabel.builder()
                    .name("Test document label")
                    .description("Test description")
                    .user(newUser)
                    .build();
            repository.save(label);
        }

        var pageable = PageRequest.of(0, numOfLabels);
        var labels = repository.findAllByUser_IdAndNameContainsIgnoreCase(
                newUser.getId(),
                "doc",
                pageable
        );

        assertNotNull(labels);
        assertEquals(numOfLabels, labels.getTotalElements());
        assertEquals(1, labels.getTotalPages());
    }

    @Test
    public void findAllLabels_By_UnknownUserId() {
        var numOfLabels = 20;
        var pageable = PageRequest.of(0, numOfLabels);
        var labels = repository.findAllByUser_IdAndNameContainsIgnoreCase(
                UUID.randomUUID().toString(),
                "",
                pageable
        );

        assertNotNull(labels);
        assertEquals(0, labels.getTotalElements());
        assertEquals(0, labels.getTotalPages());
    }

}