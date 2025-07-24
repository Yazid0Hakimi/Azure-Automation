package org.exemple.virementservice.repos;

import org.exemple.virementservice.entities.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    List<Performance> findByEmployeeId(Long employeeId);

    List<Performance> findByEmployeeIdAndReviewDateBetween(
            Long employeeId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT AVG(p.rating) FROM Performance p WHERE p.employeeId = ?1")
    Double getAverageRatingByEmployeeId(Long employeeId);

    @Query("SELECT AVG(p.rating) FROM Performance p WHERE p.employeeId = ?1 AND p.reviewDate BETWEEN ?2 AND ?3")
    Double getAverageRatingByEmployeeIdAndDateRange(Long employeeId, LocalDate startDate, LocalDate endDate);
}
