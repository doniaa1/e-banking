package com.myproject.myapp.repository;

import com.myproject.myapp.domain.InternationalTransfer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InternationalTransfer entity.
 */
@Repository
public interface InternationalTransferRepository extends JpaRepository<InternationalTransfer, Long> {
    default Optional<InternationalTransfer> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<InternationalTransfer> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<InternationalTransfer> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select internationalTransfer from InternationalTransfer internationalTransfer left join fetch internationalTransfer.bankAccount",
        countQuery = "select count(internationalTransfer) from InternationalTransfer internationalTransfer"
    )
    Page<InternationalTransfer> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select internationalTransfer from InternationalTransfer internationalTransfer left join fetch internationalTransfer.bankAccount"
    )
    List<InternationalTransfer> findAllWithToOneRelationships();

    @Query(
        "select internationalTransfer from InternationalTransfer internationalTransfer left join fetch internationalTransfer.bankAccount where internationalTransfer.id =:id"
    )
    Optional<InternationalTransfer> findOneWithToOneRelationships(@Param("id") Long id);
}
