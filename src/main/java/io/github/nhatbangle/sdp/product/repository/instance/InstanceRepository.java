package io.github.nhatbangle.sdp.product.repository.instance;

import io.github.nhatbangle.sdp.product.entity.instance.Instance;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;

@Validated
public interface InstanceRepository extends JpaRepository<Instance, String> {

    @Query("""
            select i from Instance i
            where i.moduleVersion.id = :moduleVersionId
                and (:name is null or upper(i.name) like upper(concat('%', :name, '%')))
                and (:isUsed is null or i.isUsed = :isUsed)
            """)
    @NotNull
    Page<Instance> findAllByModuleVersion_IdAndNameContainsIgnoreCaseAndIsUsed(
            @NotNull @UUID @Param("moduleVersionId") String moduleVersionId,
            @Nullable @Size(max = 150) @Param("name") String instanceName,
            @Nullable @Param("isUsed") Boolean isUsed,
            @NotNull Pageable pageable
    );

}