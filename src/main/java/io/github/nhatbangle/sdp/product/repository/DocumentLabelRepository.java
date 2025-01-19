package io.github.nhatbangle.sdp.product.repository;

import io.github.nhatbangle.sdp.product.entity.DocumentLabel;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;

@Validated
public interface DocumentLabelRepository extends JpaRepository<DocumentLabel, String> {

    @NotNull
    @Query("""
            select d from DocumentLabel d
            where d.user.id = :userId
                and (:name is null or upper(d.name) like upper(concat('%', :name, '%')))
            """)
    Page<DocumentLabel> findAllByUser_IdAndNameContainsIgnoreCase(
            @NotNull @UUID @Param("userId") String userId,
            @Nullable @Param("name") String labelName,
            @NotNull Pageable pageable
    );

}