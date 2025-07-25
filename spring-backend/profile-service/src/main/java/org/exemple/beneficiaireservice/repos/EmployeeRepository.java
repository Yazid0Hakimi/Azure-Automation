package org.exemple.beneficiaireservice.repos;

import org.exemple.beneficiaireservice.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByDepartment(String department);
    List<Employee> findByJobTitleContaining(String jobTitle);
    Employee findByEmail(String email);
}