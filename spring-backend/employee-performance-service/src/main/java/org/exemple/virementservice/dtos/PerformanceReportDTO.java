package org.exemple.virementservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceReportDTO {
    private Long employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double averageRating;
    private List<PerformanceDTO> performances;
    private List<EmployeeMetricsDTO> metrics;
    private String summaryComments;
}