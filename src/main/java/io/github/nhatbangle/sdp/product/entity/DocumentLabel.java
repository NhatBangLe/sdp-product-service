package io.github.nhatbangle.sdp.product.entity;

import io.github.nhatbangle.sdp.product.entity.module.ModuleDocumentHasDocumentLabel;
import io.github.nhatbangle.sdp.product.entity.product.ProductDocumentHasDocumentLabel;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "DOCUMENT_LABEL")
@EntityListeners(AuditingEntityListener.class)
public class DocumentLabel implements Serializable {
    @Serial
    private static final long serialVersionUID = -4505955941667700416L;

    @Id
    @Nullable
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @NotNull
    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false, length = 100)
    private String name;

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

    @NotNull
    @Size(max = 6)
    @Builder.Default
    @Column(name = "color", nullable = false, length = 6)
    private String color = "0C9DDF";

    @NotNull
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @ToString.Exclude
    @JoinColumn(name = "USER_id", nullable = false, updatable = false)
    private User user;

    @Nullable
    @OneToMany(
            mappedBy = "label",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @ToString.Exclude
    private Set<ModuleDocumentHasDocumentLabel> moduleLabels;

    @Nullable
    @OneToMany(
            mappedBy = "label",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @ToString.Exclude
    private Set<ProductDocumentHasDocumentLabel> productLabels;
}