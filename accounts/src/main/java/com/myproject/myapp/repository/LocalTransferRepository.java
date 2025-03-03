package com.myproject.myapp.repository;

import com.myproject.myapp.domain.LocalTransfer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LocalTransfer entity.
 */
@Repository
public interface LocalTransferRepository extends JpaRepository<LocalTransfer, Long> {
    default Optional<LocalTransfer> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<LocalTransfer> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<LocalTransfer> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select localTransfer from LocalTransfer localTransfer left join fetch localTransfer.bankAccount",
        countQuery = "select count(localTransfer) from LocalTransfer localTransfer"
    )
    Page<LocalTransfer> findAllWithToOneRelationships(Pageable pageable);

    @Query("select localTransfer from LocalTransfer localTransfer left join fetch localTransfer.bankAccount")
    List<LocalTransfer> findAllWithToOneRelationships();

    @Query("select localTransfer from LocalTransfer localTransfer left join fetch localTransfer.bankAccount where localTransfer.id =:id")
    Optional<LocalTransfer> findOneWithToOneRelationships(@Param("id") Long id);
}
