package io.github.nhatbangle.sdp.product.entity.module;

import io.github.nhatbangle.sdp.product.entity.Attachment;
import io.github.nhatbangle.sdp.product.entity.id.ModuleDeployingDocumentHasAttachmentId;
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
@Table(name = "MODULE_DEPLOYING_DOCUMENT_has_ATTACHMENT")
@EntityListeners(AuditingEntityListener.class)
public class ModuleDeployingDocumentHasAttachment implements Serializable {
    @Serial
    private static final long serialVersionUID = -5703874488777266140L;

    @NotNull
    @EmbeddedId
    private ModuleDeployingDocumentHasAttachmentId id;

    @NotNull
    @MapsId("documentId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DOCUMENT_id", nullable = false, updatable = false)
    private ModuleDeployingDocument document;

    @NotNull
    @MapsId("attachmentId")
    @ManyToOne(optional = false, cascade = CascadeType.ALL) // deleting a document cascades to its attachments
    @JoinColumn(name = "ATTACHMENT_id", nullable = false, updatable = false)
    private Attachment attachment;

    @Nullable
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}