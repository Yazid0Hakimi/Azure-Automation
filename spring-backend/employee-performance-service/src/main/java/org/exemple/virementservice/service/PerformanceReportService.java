package org.exemple.virementservice.service;

import org.exemple.virementservice.dtos.EmployeeMetricsDTO;
import org.exemple.virementservice.dtos.PerformanceDTO;
import org.exemple.virementservice.dtos.PerformanceReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class PerformanceReportService {

    private final PerformanceService performanceService;
    private final EmployeeMetricsService metricsService;

    @Autowired
    public PerformanceReportService(
            PerformanceService performanceService,
            EmployeeMetricsService metricsService) {
        this.performanceService = performanceService;
        this.metricsService = metricsService;
    }

    public PerformanceReportDTO generatePerformanceReport(
            Long employeeId, LocalDate startDate, LocalDate endDate) {

        // Get performance reviews for the employee within the date range
        List<PerformanceDTO> performances = performanceService.getPerformancesByEmployeeIdAndDateRange(
                employeeId, startDate, endDate);

        // Get metrics for the employee within the date range
        List<EmployeeMetricsDTO> metrics = metricsService.getMetricsByEmployeeIdAndDateRange(
                employeeId, startDate, endDate);

        // Calculate average rating
        Double averageRating = performanceService.getAverageRatingByEmployeeIdAndDateRange(
                employeeId, startDate, endDate);

        // Generate performance summary
        String summaryComments = generateSummaryComments(performances, metrics, averageRating);

        // Create and return the report DTO
        return new PerformanceReportDTO(
                employeeId,
                startDate,
                endDate,
                averageRating,
                performances,
                metrics,
                summaryComments
        );
    }

    private String generateSummaryComments(
            List<PerformanceDTO> performances,
            List<EmployeeMetricsDTO> metrics,
            Double averageRating) {

        StringBuilder summary = new StringBuilder();

        // Add average rating summary
        if (averageRating != null) {
            summary.append("Average Performance Rating: ").append(String.format("%.2f", averageRating)).append(" out of 5.\n\n");
        } else {
            summary.append("No ratings available for this period.\n\n");
        }

        // Performance review summary
        summary.append("Performance Reviews: ").append(performances.size()).append(" reviews in this period.\n");

        // Summarize strengths and areas for improvement
        if (!performances.isEmpty()) {
            summary.append("Common Strengths: ");
            performances.stream()
                    .filter(p -> p.getStrengths() != null && !p.getStrengths().isEmpty())
                    .limit(3)
                    .forEach(p -> summary.append(p.getStrengths().split("\\.")[0]).append("; "));
            summary.append("\n");

            summary.append("Areas for Improvement: ");
            performances.stream()
                    .filter(p -> p.getAreasForImprovement() != null && !p.getAreasForImprovement().isEmpty())
                    .limit(3)
                    .forEach(p -> summary.append(p.getAreasForImprovement().split("\\.")[0]).append("; "));
            summary.append("\n\n");
        }

        // Metrics summary
        if (!metrics.isEmpty()) {
            // Calculate averages for numeric metrics
            double avgTasksCompleted = metrics.stream()
                    .filter(m -> m.getTasksCompleted() != null)
                    .mapToInt(EmployeeMetricsDTO::getTasksCompleted)
                    .average()
                    .orElse(0);

            double avgProjectsCompleted = metrics.stream()
                    .filter(m -> m.getProjectsCompleted() != null)
                    .mapToInt(EmployeeMetricsDTO::getProjectsCompleted)
                    .average()
                    .orElse(0);

            double avgClientSatisfaction = metrics.stream()
                    .filter(m -> m.getClientSatisfactionScore() != null)
                    .mapToInt(EmployeeMetricsDTO::getClientSatisfactionScore)
                    .average()
                    .orElse(0);

            summary.append("Performance Metrics Summary:\n");
            summary.append("- Average Tasks Completed: ").append(String.format("%.1f", avgTasksCompleted)).append("\n");
            summary.append("- Average Projects Completed: ").append(String.format("%.1f", avgProjectsCompleted)).append("\n");
            summary.append("- Average Client Satisfaction: ").append(String.format("%.1f", avgClientSatisfaction)).append("/5\n");
            summary.append("- Average Client Satisfaction: ").append(String.format("%.1f", avgClientSatisfaction)).append("/5\n");
        } else {
            summary.append("No performance metrics recorded for this period.\n");
        }

        return summary.toString();
    }
}