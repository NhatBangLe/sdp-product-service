package io.github.nhatbangle.sdp.product.repository;

import io.github.nhatbangle.sdp.product.entity.DocumentLabel;
import io.github.nhatbangle.sdp.product.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class DocumentLabelRepositoryTests extends RepositoryTests {

    @Autowired
    private DocumentLabelRepository labelRepository;
    @Autowired
    private UserRepository userRepository;

    private static final User newUser = User.builder().id(UUID.randomUUID().toString()).build();

    @Test
    public void createLabel_with_NewUser() {
        var name = "Test";
        var description = "Test description";
        var savedLabel = createLabel(name, description);

        log.info("Saved label: {}", savedLabel);
        assertNotNull(savedLabel.getId());
        assertEquals(newUser.getId(), savedLabel.getUser().getId());
    }

    @Test
    public void deleteLabel_without_deleteUser() {
        var userId = newUser.getId();
        var name = "Test label";
        var description = "Test label description";
        var productId = createLabel(name, description).getId();

        log.info("Created label with ID: {}", productId);
        assertNotNull(productId);

        labelRepository.deleteById(productId);
        log.info("Deleted label with ID: {}", productId);
        assertNull(labelRepository.findById(productId).orElse(null));
        assertNotNull(userRepository.findById(userId).orElse(null));
    }

    @ParameterizedTest
    @MethodSource(value = "findAllLabels_TestSource")
    void findAllByUser_IdAndNameContainsIgnoreCase(
            int numOfInitElements,
            String userId,
            String labelName,
            int expectedTotalElements
    ) {
        for (int i = 0; i < numOfInitElements; i++) {
            var name = "Test label " + i;
            var description = "Test label description " + i;
            createLabel(name, description);
        }

        var labels = labelRepository.findAllByUser_IdAndNameContainsIgnoreCase(
                userId,
                labelName,
                PageRequest.of(0, Math.max(expectedTotalElements, 1))
        );

        assertNotNull(labels);
        assertEquals(expectedTotalElements, labels.getTotalElements());
    }

    private static Stream<Arguments> findAllLabels_TestSource() {
        var unknownUserId = UUID.randomUUID().toString();
        var createdUserId = newUser.getId();

        return Stream.of(
                Arguments.of(1, unknownUserId, "", 0),
                Arguments.of(5, unknownUserId, "", 0),
                Arguments.of(1, unknownUserId, "Test", 0),
                Arguments.of(5, unknownUserId, "Test", 0),
                Arguments.of(1, unknownUserId, "la", 0),
                Arguments.of(5, unknownUserId, "la", 0),
                Arguments.of(1, unknownUserId, null, 0),
                Arguments.of(5, unknownUserId, null, 0),

                Arguments.of(1, createdUserId, "", 1),
                Arguments.of(5, createdUserId, "", 5),
                Arguments.of(1, createdUserId, "Test", 1),
                Arguments.of(5, createdUserId, "Test", 5),
                Arguments.of(1, createdUserId, "la", 1),
                Arguments.of(5, createdUserId, "la", 5),
                Arguments.of(1, createdUserId, null, 1),
                Arguments.of(5, createdUserId, null, 5)
        );
    }

    @NotNull
    private DocumentLabel createLabel(String name, String description) {
        var label = DocumentLabel.builder()
                .name(name)
                .description(description)
                .user(newUser)
                .build();

        return labelRepository.save(label);
    }

}