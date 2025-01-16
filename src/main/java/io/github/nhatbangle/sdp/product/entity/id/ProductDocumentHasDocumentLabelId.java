package io.github.nhatbangle.sdp.product.entity.id;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.Hibernate;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class ProductDocumentHasDocumentLabelId implements Serializable {
    @Serial
    private static final long serialVersionUID = 5861343938612351603L;

    @UUID
    @NotNull
    private String documentId;

    @Min(0)
    @NotNull
    private Integer labelId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductDocumentHasDocumentLabelId entity = (ProductDocumentHasDocumentLabelId) o;
        return Objects.equals(this.documentId, entity.documentId) &&
               Objects.equals(this.labelId, entity.labelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentId, labelId);
    }
}