package org.exemple.virementservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "employee_metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Metric date is required")
    private LocalDate metricDate;

    private Integer tasksCompleted;

    private Integer projectsCompleted;

    private Double avgTaskCompletionTime;

    private Integer clientSatisfactionScore;

    private Integer teamCollaborationScore;

    private Double innovationScore;

    private String metricNotes;
}
