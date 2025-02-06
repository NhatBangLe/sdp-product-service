package io.github.nhatbangle.sdp.product.repository.product;

import io.github.nhatbangle.sdp.product.entity.Attachment;
import io.github.nhatbangle.sdp.product.entity.DocumentLabel;
import io.github.nhatbangle.sdp.product.entity.User;
import io.github.nhatbangle.sdp.product.entity.id.ProductDocumentHasAttachmentId;
import io.github.nhatbangle.sdp.product.entity.id.ProductDocumentHasDocumentLabelId;
import io.github.nhatbangle.sdp.product.entity.product.Product;
import io.github.nhatbangle.sdp.product.entity.product.ProductDocument;
import io.github.nhatbangle.sdp.product.entity.product.ProductDocumentHasAttachment;
import io.github.nhatbangle.sdp.product.entity.product.ProductDocumentHasDocumentLabel;
import io.github.nhatbangle.sdp.product.repository.DocumentLabelRepository;
import io.github.nhatbangle.sdp.product.repository.RepositoryTests;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
class ProductDocumentRepositoryTests extends RepositoryTests {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductDocumentRepository documentRepository;
    @Autowired
    private DocumentLabelRepository labelRepository;

    private static final String userId = UUID.randomUUID().toString();
    private final User user = User.builder().id(userId).build();

    @Test
    void createNewDocument_with_Attachments_And_Labels() {
        var product = createProduct("Test create new product");
        assertNotNull(product.getId());

        String title = "Document title", description = "Document description";
        var document = createDocument(title, description);
        assertNotNull(document.getId());

        int numOfAttachments = 10, numOfLabels = 10;
        document.setProduct(product);
        document.setAttachments(createAttachments(document, numOfAttachments));
        document.setLabels(createLabels(document, numOfLabels));

        var savedDocument = documentRepository.save(document);

        assertEquals(product, savedDocument.getProduct());

        var attachments = savedDocument.getAttachments();
        assertNotNull(attachments);
        assertEquals(numOfAttachments, attachments.size());

        var labels = savedDocument.getLabels();
        assertNotNull(labels);
        assertEquals(numOfLabels, labels.size());
    }

    @Test
    void deleteDocument_without_deleteProduct() {
        var product = createProduct("Test create new product");
        assertNotNull(product.getId());

        String title = "Document title", description = "Document description";
        var document = createDocument(title, description);
        var documentId = document.getId();
        assertNotNull(documentId);

        int numOfAttachments = 10, numOfLabels = 10;
        document.setProduct(product);
        document.setAttachments(createAttachments(document, numOfAttachments));
        document.setLabels(createLabels(document, numOfLabels));

        var savedDocument = documentRepository.save(document);

        assertEquals(product, savedDocument.getProduct());

        var attachments = savedDocument.getAttachments();
        assertNotNull(attachments);
        assertEquals(numOfAttachments, attachments.size());

        var labels = savedDocument.getLabels();
        assertNotNull(labels);
        assertEquals(numOfLabels, labels.size());

        documentRepository.delete(savedDocument);
        log.info("Deleted document with ID: {}", documentId);
        assertNotNull(productRepository.findById(product.getId()).orElse(null));
        assertNull(documentRepository.findById(documentId).orElse(null));
    }

    @ParameterizedTest
    @MethodSource(value = "findAllProducts_TestSource")
    void findAllByUser_IdAndProduct_NameAndTitleContainsIgnoreCase(
            int totalDocuments,
            String userId,
            String productName,
            String documentTitle,
            int expectedTotalElements
    ) {
        var product = createProduct("Test product");
        assertNotNull(product.getId());

        int numOfAttachments = 10, numOfLabels = 10;
        for (int i = 0; i < totalDocuments; i++) {
            String title = "Document title", description = "Document description";
            var document = createDocument(title, description);
            assertNotNull(document.getId());

            document.setProduct(product);
            document.setAttachments(createAttachments(document, numOfAttachments));
            document.setLabels(createLabels(document, numOfLabels));
            documentRepository.save(document);
        }

        var documents = documentRepository.findAllByUser_IdAndProduct_NameAndTitleContainsIgnoreCase(
                userId,
                productName,
                documentTitle,
                PageRequest.of(0, Math.max(expectedTotalElements, 1))
        );

        assertNotNull(documents);
        assertEquals(expectedTotalElements, documents.getTotalElements());

        documents.stream().forEach(document -> {
            assertEquals(product, document.getProduct());

            var attachments = document.getAttachments();
            assertNotNull(attachments);
            assertEquals(numOfAttachments, attachments.size());

            var labels = document.getLabels();
            assertNotNull(labels);
            assertEquals(numOfLabels, labels.size());
        });
    }

    private static Stream<Arguments> findAllProducts_TestSource() {
        var unknownUserId = UUID.randomUUID().toString();
        var createdUserId = userId;

        return Stream.of(
                Arguments.of(1, unknownUserId, null, null, 0),
                Arguments.of(1, unknownUserId, "test", null, 0),
                Arguments.of(1, unknownUserId, "pro", null, 0),
                Arguments.of(1, unknownUserId, null, "doc", 0),
                Arguments.of(1, unknownUserId, null, "tit", 0),
                Arguments.of(1, unknownUserId, "test", "doc", 0),
                Arguments.of(1, unknownUserId, "test", "tit", 0),
                Arguments.of(1, unknownUserId, "pro", "doc", 0),
                Arguments.of(1, unknownUserId, "pro", "tit", 0),

                Arguments.of(5, createdUserId, null, null, 5),
                Arguments.of(5, createdUserId, "test", null, 5),
                Arguments.of(5, createdUserId, "pro", null, 5),
                Arguments.of(5, createdUserId, null, "doc", 5),
                Arguments.of(5, createdUserId, null, "tit", 5),
                Arguments.of(5, createdUserId, "test", "doc", 5),
                Arguments.of(5, createdUserId, "test", "tit", 5),
                Arguments.of(5, createdUserId, "pro", "doc", 5),
                Arguments.of(5, createdUserId, "pro", "tit", 5)
        );
    }

    private Set<ProductDocumentHasAttachment> createAttachments(
            @NotNull ProductDocument document,
            int total
    ) {
        var documentId = document.getId();
        assertNotNull(documentId);

        var usedIds = new HashSet<String>(total);
        var set = new HashSet<ProductDocumentHasAttachment>(total);
        for (int i = 0; i < total; i++) {
            String attachmentId;
            do {
                attachmentId = UUID.randomUUID().toString();
            } while (usedIds.contains(attachmentId));
            usedIds.add(attachmentId);

            var attachment = ProductDocumentHasAttachment.builder()
                    .id(ProductDocumentHasAttachmentId.builder()
                            .attachmentId(attachmentId)
                            .documentId(documentId)
                            .build())
                    .attachment(Attachment.builder().id(attachmentId).build())
                    .document(document)
                    .build();
            set.add(attachment);
        }
        return set;
    }

    private Set<ProductDocumentHasDocumentLabel> createLabels(
            @NotNull ProductDocument document,
            int total
    ) {
        var documentId = document.getId();
        assertNotNull(documentId);

        var set = new HashSet<ProductDocumentHasDocumentLabel>(total);
        for (int i = 0; i < total; i++) {
            var documentLabel = createDocumentLabel();
            assertNotNull(documentLabel.getId());

            var label = ProductDocumentHasDocumentLabel.builder()
                    .id(ProductDocumentHasDocumentLabelId.builder()
                            .labelId(documentLabel.getId())
                            .documentId(documentId)
                            .build())
                    .label(documentLabel)
                    .document(document)
                    .numOrder((short) i)
                    .build();
            set.add(label);
        }
        return set;
    }

    private DocumentLabel createDocumentLabel() {
        return labelRepository.save(DocumentLabel.builder()
                .name("Test label")
                .description("Test label description")
                .user(user)
                .build());
    }

    private Product createProduct(String productName) {
        return productRepository.save(Product.builder()
                .name(productName)
                .description("Test product description")
                .user(user)
                .build());
    }

    private ProductDocument createDocument(String title, String description) {
        return documentRepository.save(ProductDocument.builder()
                .title(title)
                .description(description)
                .user(user)
                .build());
    }

}