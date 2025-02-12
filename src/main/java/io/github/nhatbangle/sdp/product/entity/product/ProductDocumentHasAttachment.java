package io.github.nhatbangle.sdp.product.entity.product;

import io.github.nhatbangle.sdp.product.entity.Attachment;
import io.github.nhatbangle.sdp.product.entity.id.ProductDocumentHasAttachmentId;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PRODUCT_DOCUMENT_has_ATTACHMENT")
@EntityListeners(AuditingEntityListener.class)
public class ProductDocumentHasAttachment implements Serializable {
    @Serial
    private static final long serialVersionUID = 3852497200591478329L;

    @NotNull
    @EmbeddedId
    private ProductDocumentHasAttachmentId id;

    @NotNull
    @MapsId("documentId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DOCUMENT_id", nullable = false, updatable = false)
    private ProductDocument document;

    @NotNull
    @MapsId("attachmentId")
    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "ATTACHMENT_id", nullable = false, updatable = false)
    private Attachment attachment;

    @Nullable
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}