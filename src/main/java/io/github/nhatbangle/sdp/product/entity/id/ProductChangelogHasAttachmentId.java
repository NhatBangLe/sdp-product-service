package io.github.nhatbangle.sdp.product.entity.id;

import jakarta.persistence.Embeddable;
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
public class ProductChangelogHasAttachmentId implements Serializable {
    @Serial
    private static final long serialVersionUID = 8796759884551119226L;

    @UUID
    @NotNull
    private String changelogId;

    @UUID
    @NotNull
    private String attachmentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductChangelogHasAttachmentId entity = (ProductChangelogHasAttachmentId) o;
        return Objects.equals(this.changelogId, entity.changelogId) &&
               Objects.equals(this.attachmentId, entity.attachmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(changelogId, attachmentId);
    }
}