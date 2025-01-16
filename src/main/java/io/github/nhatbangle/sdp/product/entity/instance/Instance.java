package io.github.nhatbangle.sdp.product.entity.instance;

import io.github.nhatbangle.sdp.product.entity.module.ModuleVersion;
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
@Table(name = "INSTANCE")
@EntityListeners(AuditingEntityListener.class)
public class Instance implements Serializable {
    @Serial
    private static final long serialVersionUID = -5265538427114071433L;

    @Id
    @Nullable
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @NotNull
    @Builder.Default
    @Column(name = "is_used", nullable = false)
    private Boolean isUsed = false;

    @NotNull
    @NotBlank
    @Size(max = 150)
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Nullable
    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

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
    @JoinColumn(name = "MODULE_VERSION_id", nullable = false, referencedColumnName = "id")
    private ModuleVersion moduleVersion;

    @Nullable
    @OneToMany(
            mappedBy = "instance",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<InstanceAttribute> attributes;
}