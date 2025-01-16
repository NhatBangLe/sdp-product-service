package io.github.nhatbangle.sdp.product.entity.product;

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
@Table(name = "PRODUCT_CHANGELOG")
@EntityListeners(AuditingEntityListener.class)
public class ProductChangelog implements Serializable {
    @Serial
    private static final long serialVersionUID = -2418388294892499493L;

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

    @Lob
    @Nullable
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
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUCT_VERSION_id", nullable = false, referencedColumnName = "id")
    private ProductVersion productVersion;

    @Nullable
    @OneToMany(
            mappedBy = "changelog",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<ProductChangelogHasAttachment> attachments;
}