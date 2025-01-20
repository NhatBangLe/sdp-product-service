package io.github.nhatbangle.sdp.product.repository.instance;

import io.github.nhatbangle.sdp.product.entity.instance.InstanceAttribute;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface InstanceAttributeRepository extends JpaRepository<InstanceAttribute, String> {

    @NotNull
    List<InstanceAttribute> findAllByInstance_Id(
            @NotNull @UUID @Param("instanceId") String instanceId,
            @Nullable Sort sort
    );

}