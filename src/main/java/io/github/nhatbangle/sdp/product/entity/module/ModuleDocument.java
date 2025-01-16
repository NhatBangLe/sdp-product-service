package io.github.nhatbangle.sdp.product.entity.module;

import io.github.nhatbangle.sdp.product.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import jakarta.validation.constraints.NotNull;
import jakarta.annotation.Nullable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "MODULE_DOCUMENT")
@EntityListeners(AuditingEntityListener.class)
public class ModuleDocument implements Serializable {
    @Serial
    private static final long serialVersionUID = -4317074473833407031L;

    @Id
    @Nullable
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @NotNull
    @NotBlank
    @Size(max = 150)
    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Nullable
    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Nullable
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Nullable
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "MODULE_id")
    private Module module;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_id", nullable = false, updatable = false)
    private User user;

    @Nullable
    @OneToMany(
            mappedBy = "document",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<ModuleDocumentHasDocumentLabel> labels;

    @Nullable
    @OneToMany(
            mappedBy = "document",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<ModuleDocumentHasAttachment> attachments;
}