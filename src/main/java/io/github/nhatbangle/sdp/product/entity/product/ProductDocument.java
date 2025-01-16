package io.github.nhatbangle.sdp.product.entity.product;

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
@Table(name = "PRODUCT_DOCUMENT")
@EntityListeners(AuditingEntityListener.class)
public class ProductDocument implements Serializable {
    @Serial
    private static final long serialVersionUID = 9084393074634431340L;

    @Id
    @Nullable
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

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
    @JoinColumn(name = "PRODUCT_id")
    private Product product;

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
    private Set<ProductDocumentHasDocumentLabel> labels;

    @Nullable
    @OneToMany(
            mappedBy = "document",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<ProductDocumentHasAttachment> attachments;
}