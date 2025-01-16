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
public class ModuleChangelogHasAttachmentId implements Serializable {
    @Serial
    private static final long serialVersionUID = 5280387223586058006L;

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
        ModuleChangelogHasAttachmentId entity = (ModuleChangelogHasAttachmentId) o;
        return Objects.equals(this.attachmentId, entity.attachmentId) &&
               Objects.equals(this.changelogId, entity.changelogId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attachmentId, changelogId);
    }
}