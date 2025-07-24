package org.exemple.virementservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeMetricsDTO {
    private Long id;
    private Long employeeId;
    private LocalDate metricDate;
    private Integer tasksCompleted;
    private Integer projectsCompleted;
    private Double avgTaskCompletionTime;
    private Integer clientSatisfactionScore;
    private Integer teamCollaborationScore;
    private Double innovationScore;
    private String metricNotes;
}
