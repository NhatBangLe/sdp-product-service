package io.github.nhatbangle.sdp.product.entity.module;

import io.github.nhatbangle.sdp.product.entity.DocumentLabel;
import io.github.nhatbangle.sdp.product.entity.id.ModuleDocumentHasDocumentLabelId;
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
@Table(name = "MODULE_DOCUMENT_has_DOCUMENT_LABEL")
public class ModuleDocumentHasDocumentLabel implements Serializable {
    @Serial
    private static final long serialVersionUID = -2141035897155358802L;

    @NotNull
    @EmbeddedId
    private ModuleDocumentHasDocumentLabelId id;

    @NotNull
    @MapsId("documentId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DOCUMENT_id", nullable = false, updatable = false)
    private ModuleDocument document;

    @NotNull
    @MapsId("labelId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "LABEL_id", nullable = false, updatable = false)
    private DocumentLabel label;

    @Min(0)
    @NotNull
    @Column(name = "num_order", nullable = false)
    private Short numOrder;
}