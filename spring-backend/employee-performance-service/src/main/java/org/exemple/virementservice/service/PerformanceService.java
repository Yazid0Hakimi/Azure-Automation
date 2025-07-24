package org.exemple.virementservice.service;

import feign.FeignException;
import org.exemple.virementservice.dtos.PerformanceDTO;
import org.exemple.virementservice.entities.Performance;
import org.exemple.virementservice.feign.EmployeeProfileClient;
import org.exemple.virementservice.repos.PerformanceRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final EmployeeProfileClient employeeProfileClient;
    @Autowired
    public PerformanceService(PerformanceRepository performanceRepository, EmployeeProfileClient employeeProfileClient) {
        this.performanceRepository = performanceRepository;
        this.employeeProfileClient = employeeProfileClient;
    }

//    public PerformanceDTO createPerformance(PerformanceDTO performanceDTO) {
//        Performance performance = new Performance();
//        BeanUtils.copyProperties(performanceDTO, performance);
//        Performance savedPerformance = performanceRepository.save(performance);
//        PerformanceDTO savedDTO = new PerformanceDTO();
//        BeanUtils.copyProperties(savedPerformance, savedDTO);
//        return savedDTO;
//    }

    public PerformanceDTO getPerformanceById(Long id) {
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Performance record not found with id: " + id));
        PerformanceDTO performanceDTO = new PerformanceDTO();
        BeanUtils.copyProperties(performance, performanceDTO);
        return performanceDTO;
    }

    public List<PerformanceDTO> getAllPerformances() {
        return performanceRepository.findAll().stream()
                .map(performance -> {
                    PerformanceDTO dto = new PerformanceDTO();
                    BeanUtils.copyProperties(performance, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<PerformanceDTO> getPerformancesByEmployeeId(Long employeeId) {
        return performanceRepository.findByEmployeeId(employeeId).stream()
                .map(performance -> {
                    PerformanceDTO dto = new PerformanceDTO();
                    BeanUtils.copyProperties(performance, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<PerformanceDTO> getPerformancesByEmployeeIdAndDateRange(
            Long employeeId, LocalDate startDate, LocalDate endDate) {
        return performanceRepository.findByEmployeeIdAndReviewDateBetween(employeeId, startDate, endDate).stream()
                .map(performance -> {
                    PerformanceDTO dto = new PerformanceDTO();
                    BeanUtils.copyProperties(performance, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public PerformanceDTO updatePerformance(Long id, PerformanceDTO performanceDTO) {
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Performance record not found with id: " + id));
        BeanUtils.copyProperties(performanceDTO, performance);
        performance.setId(id); // Ensure ID is not overwritten
        Performance updatedPerformance = performanceRepository.save(performance);
        PerformanceDTO updatedDTO = new PerformanceDTO();
        BeanUtils.copyProperties(updatedPerformance, updatedDTO);
        return updatedDTO;
    }

    public void deletePerformance(Long id) {
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Performance record not found with id: " + id));
        performanceRepository.delete(performance);
    }

    public Double getAverageRatingByEmployeeId(Long employeeId) {
        return performanceRepository.getAverageRatingByEmployeeId(employeeId);
    }

    public Double getAverageRatingByEmployeeIdAndDateRange(
            Long employeeId, LocalDate startDate, LocalDate endDate) {
        return performanceRepository.getAverageRatingByEmployeeIdAndDateRange(employeeId, startDate, endDate);
    }

    public PerformanceDTO createPerformance(PerformanceDTO performanceDTO) {
        // Validate that employee exists
        validateEmployeeExists(performanceDTO.getEmployeeId());

        Performance performance = new Performance();
        BeanUtils.copyProperties(performanceDTO, performance);
        Performance savedPerformance = performanceRepository.save(performance);
        PerformanceDTO savedDTO = new PerformanceDTO();
        BeanUtils.copyProperties(savedPerformance, savedDTO);
        return savedDTO;
    }

    private void validateEmployeeExists(Long employeeId) {
        try {
            Boolean exists = employeeProfileClient.checkEmployeeExists(employeeId);
            if (exists == null || !exists) {
                throw new RuntimeException("Employee with ID " + employeeId + " does not exist");
            }
        } catch (FeignException e) {
            throw new RuntimeException("Error verifying employee existence: " + e.getMessage());
        }
    }

}
