package com.myproject.myapp.repository;

import com.myproject.myapp.domain.AnalysisReport;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AnalysisReport entity.
 */
@Repository
public interface AnalysisReportRepository extends JpaRepository<AnalysisReport, Long> {
    default Optional<AnalysisReport> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AnalysisReport> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AnalysisReport> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select analysisReport from AnalysisReport analysisReport left join fetch analysisReport.dataCollection",
        countQuery = "select count(analysisReport) from AnalysisReport analysisReport"
    )
    Page<AnalysisReport> findAllWithToOneRelationships(Pageable pageable);

    @Query("select analysisReport from AnalysisReport analysisReport left join fetch analysisReport.dataCollection")
    List<AnalysisReport> findAllWithToOneRelationships();

    @Query(
        "select analysisReport from AnalysisReport analysisReport left join fetch analysisReport.dataCollection where analysisReport.id =:id"
    )
    Optional<AnalysisReport> findOneWithToOneRelationships(@Param("id") Long id);
}
