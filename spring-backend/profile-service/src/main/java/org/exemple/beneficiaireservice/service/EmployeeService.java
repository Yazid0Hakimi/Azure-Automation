package org.exemple.beneficiaireservice.service;


import org.exemple.beneficiaireservice.dtos.EmployeeDTO;
import org.exemple.beneficiaireservice.entities.Employee;
import org.exemple.beneficiaireservice.repos.EmployeeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        Employee savedEmployee = employeeRepository.save(employee);
        EmployeeDTO savedDTO = new EmployeeDTO();
        BeanUtils.copyProperties(savedEmployee, savedDTO);
        return savedDTO;
    }

    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employee -> {
                    EmployeeDTO dto = new EmployeeDTO();
                    BeanUtils.copyProperties(employee, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setId(id); // Ensure ID is not overwritten
        Employee updatedEmployee = employeeRepository.save(employee);
        EmployeeDTO updatedDTO = new EmployeeDTO();
        BeanUtils.copyProperties(updatedEmployee, updatedDTO);
        return updatedDTO;
    }

    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        employeeRepository.delete(employee);
    }

    public List<EmployeeDTO> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartment(department).stream()
                .map(employee -> {
                    EmployeeDTO dto = new EmployeeDTO();
                    BeanUtils.copyProperties(employee, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<EmployeeDTO> getEmployeesByJobTitle(String jobTitle) {
        return employeeRepository.findByJobTitleContaining(jobTitle).stream()
                .map(employee -> {
                    EmployeeDTO dto = new EmployeeDTO();
                    BeanUtils.copyProperties(employee, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public boolean checkEmployeeExists(Long id) {
        return employeeRepository.existsById(id);
    }

}
