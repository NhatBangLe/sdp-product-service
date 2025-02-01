package io.github.nhatbangle.sdp.product.entity.module;

import io.github.nhatbangle.sdp.product.entity.Attachment;
import io.github.nhatbangle.sdp.product.entity.id.ModuleChangelogHasAttachmentId;
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
@Table(name = "MODULE_CHANGELOG_has_ATTACHMENT")
@EntityListeners(AuditingEntityListener.class)
public class ModuleChangelogHasAttachment implements Serializable {
    @Serial
    private static final long serialVersionUID = -388448136242330179L;

    @NotNull
    @EmbeddedId
    private ModuleChangelogHasAttachmentId id;

    @NotNull
    @MapsId("changelogId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MODULE_CHANGELOG_id", nullable = false, updatable = false)
    private ModuleChangelog changelog;

    @NotNull
    @MapsId("attachmentId")
    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "ATTACHMENT_id", nullable = false, updatable = false)
    private Attachment attachment;

    @Nullable
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}