package org.exemple.virementservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceDTO {
    private Long id;
    private Long employeeId;
    private LocalDate reviewDate;
    private Integer rating;
    private String reviewerName;
    private String comments;
    private String goals;
    private String strengths;
    private String areasForImprovement;
}