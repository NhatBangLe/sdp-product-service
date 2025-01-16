package io.github.nhatbangle.sdp.product.entity.product;

import io.github.nhatbangle.sdp.product.entity.DocumentLabel;
import io.github.nhatbangle.sdp.product.entity.id.ProductDocumentHasDocumentLabelId;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PRODUCT_DOCUMENT_has_DOCUMENT_LABEL")
public class ProductDocumentHasDocumentLabel implements Serializable {
    @Serial
    private static final long serialVersionUID = -3978313624542701975L;

    @NotNull
    @EmbeddedId
    private ProductDocumentHasDocumentLabelId id;

    @NotNull
    @MapsId("documentId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DOCUMENT_id", nullable = false, updatable = false)
    private ProductDocument document;

    @NotNull
    @MapsId("labelId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "LABEL_id", nullable = false, updatable = false)
    private DocumentLabel label;

    @Min(0)
    @NotNull
    @Column(name = "num_order", nullable = false, updatable = false)
    private Short numOrder;
}