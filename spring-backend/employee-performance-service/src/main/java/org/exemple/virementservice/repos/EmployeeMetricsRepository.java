package org.exemple.virementservice.repos;

import org.exemple.virementservice.entities.EmployeeMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeMetricsRepository extends JpaRepository<EmployeeMetrics, Long> {
    List<EmployeeMetrics> findByEmployeeId(Long employeeId);

    List<EmployeeMetrics> findByEmployeeIdAndMetricDateBetween(
            Long employeeId, LocalDate startDate, LocalDate endDate);
}
