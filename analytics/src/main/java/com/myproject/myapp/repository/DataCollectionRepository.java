package com.myproject.myapp.repository;

import com.myproject.myapp.domain.DataCollection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DataCollection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataCollectionRepository extends JpaRepository<DataCollection, Long> {}
