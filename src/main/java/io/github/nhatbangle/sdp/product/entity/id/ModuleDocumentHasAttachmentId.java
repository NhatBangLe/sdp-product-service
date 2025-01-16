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
public class ModuleDocumentHasAttachmentId implements Serializable {
    @Serial
    private static final long serialVersionUID = -8688393264052518731L;

    @UUID
    @NotNull
    private String documentId;

    @UUID
    @NotNull
    private String attachmentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ModuleDocumentHasAttachmentId entity = (ModuleDocumentHasAttachmentId) o;
        return Objects.equals(this.documentId, entity.documentId) &&
               Objects.equals(this.attachmentId, entity.attachmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentId, attachmentId);
    }
}