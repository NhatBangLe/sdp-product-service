package io.github.nhatbangle.sdp.product.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.UUID;

import java.io.Serial;
import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ATTACHMENT")
public class Attachment implements Serializable {
    @Serial
    private static final long serialVersionUID = -8702604788719205389L;

    @Id
    @UUID
    @NotNull
    @Column(name = "id", nullable = false, length = 36)
    private String id;
}