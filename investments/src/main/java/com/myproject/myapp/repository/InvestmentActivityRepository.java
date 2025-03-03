package com.myproject.myapp.repository;

import com.myproject.myapp.domain.InvestmentActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InvestmentActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvestmentActivityRepository extends JpaRepository<InvestmentActivity, Long> {}
