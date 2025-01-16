package io.github.nhatbangle.sdp.product.entity;

import io.github.nhatbangle.sdp.product.entity.module.ModuleDeployingDocument;
import io.github.nhatbangle.sdp.product.entity.module.ModuleDocument;
import io.github.nhatbangle.sdp.product.entity.product.Product;
import io.github.nhatbangle.sdp.product.entity.product.ProductDocument;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.UUID;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "USER")
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = -3019093013145459824L;

    @Id
    @UUID
    @NotNull
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Nullable
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DocumentLabel> labels;

    @Nullable
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ModuleDeployingDocument> moduleDeployingDocuments;

    @Nullable
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ModuleDocument> moduleDocuments;

    @Nullable
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Product> products;

    @Nullable
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductDocument> productDocuments;
}