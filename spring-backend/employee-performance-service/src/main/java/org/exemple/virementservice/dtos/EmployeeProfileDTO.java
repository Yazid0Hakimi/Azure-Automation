package org.exemple.virementservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeProfileDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String jobTitle;
    private String department;
    private LocalDate hireDate;
    private String managerName;
}