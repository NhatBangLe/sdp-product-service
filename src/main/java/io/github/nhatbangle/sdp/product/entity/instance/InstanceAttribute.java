package io.github.nhatbangle.sdp.product.entity.instance;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Table(name = "INSTANCE_ATTRIBUTE")
public class InstanceAttribute implements Serializable {
    @Serial
    private static final long serialVersionUID = -3463784953349908510L;

    @Id
    @Nullable
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Min(0)
    @NotNull
    @Column(name = "num_order", nullable = false)
    private Short numOrder;

    @NotNull
    @NotBlank
    @Size(max = 150)
    @Column(name = "key", nullable = false, length = 150)
    private String key;

    @NotNull
    @Size(max = 255)
    @Column(name = "value", nullable = false)
    private String value;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "INSTANCE_id", nullable = false, updatable = false)
    private Instance instance;
}