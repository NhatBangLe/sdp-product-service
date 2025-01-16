package io.github.nhatbangle.sdp.product.entity.product;

import io.github.nhatbangle.sdp.product.entity.Attachment;
import io.github.nhatbangle.sdp.product.entity.id.ProductChangelogHasAttachmentId;
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
@Table(name = "PRODUCT_CHANGELOG_has_ATTACHMENT")
@EntityListeners(AuditingEntityListener.class)
public class ProductChangelogHasAttachment implements Serializable {
    @Serial
    private static final long serialVersionUID = -2520128368985531728L;

    @NotNull
    @EmbeddedId
    private ProductChangelogHasAttachmentId id;

    @NotNull
    @MapsId("changelogId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUCT_CHANGELOG_id", nullable = false, updatable = false)
    private ProductChangelog changelog;

    @NotNull
    @MapsId("attachmentId")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "ATTACHMENT_id", nullable = false, updatable = false)
    private Attachment attachment;

    @Nullable
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}