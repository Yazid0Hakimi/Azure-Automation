package org.exemple.virementservice.service;

import org.exemple.virementservice.dtos.EmployeeMetricsDTO;
import org.exemple.virementservice.entities.EmployeeMetrics;
import org.exemple.virementservice.repos.EmployeeMetricsRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeMetricsService {

    private final EmployeeMetricsRepository metricsRepository;

    @Autowired
    public EmployeeMetricsService(EmployeeMetricsRepository metricsRepository) {
        this.metricsRepository = metricsRepository;
    }

    public EmployeeMetricsDTO createMetrics(EmployeeMetricsDTO metricsDTO) {
        EmployeeMetrics metrics = new EmployeeMetrics();
        BeanUtils.copyProperties(metricsDTO, metrics);
        EmployeeMetrics savedMetrics = metricsRepository.save(metrics);
        EmployeeMetricsDTO savedDTO = new EmployeeMetricsDTO();
        BeanUtils.copyProperties(savedMetrics, savedDTO);
        return savedDTO;
    }

    public EmployeeMetricsDTO getMetricsById(Long id) {
        EmployeeMetrics metrics = metricsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Metrics record not found with id: " + id));
        EmployeeMetricsDTO metricsDTO = new EmployeeMetricsDTO();
        BeanUtils.copyProperties(metrics, metricsDTO);
        return metricsDTO;
    }

    public List<EmployeeMetricsDTO> getAllMetrics() {
        return metricsRepository.findAll().stream()
                .map(metrics -> {
                    EmployeeMetricsDTO dto = new EmployeeMetricsDTO();
                    BeanUtils.copyProperties(metrics, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<EmployeeMetricsDTO> getMetricsByEmployeeId(Long employeeId) {
        return metricsRepository.findByEmployeeId(employeeId).stream()
                .map(metrics -> {
                    EmployeeMetricsDTO dto = new EmployeeMetricsDTO();
                    BeanUtils.copyProperties(metrics, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<EmployeeMetricsDTO> getMetricsByEmployeeIdAndDateRange(
            Long employeeId, LocalDate startDate, LocalDate endDate) {
        return metricsRepository.findByEmployeeIdAndMetricDateBetween(employeeId, startDate, endDate).stream()
                .map(metrics -> {
                    EmployeeMetricsDTO dto = new EmployeeMetricsDTO();
                    BeanUtils.copyProperties(metrics, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public EmployeeMetricsDTO updateMetrics(Long id, EmployeeMetricsDTO metricsDTO) {
        EmployeeMetrics metrics = metricsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Metrics record not found with id: " + id));
        BeanUtils.copyProperties(metricsDTO, metrics);
        metrics.setId(id); // Ensure ID is not overwritten
        EmployeeMetrics updatedMetrics = metricsRepository.save(metrics);
        EmployeeMetricsDTO updatedDTO = new EmployeeMetricsDTO();
        BeanUtils.copyProperties(updatedMetrics, updatedDTO);
        return updatedDTO;
    }

    public void deleteMetrics(Long id) {
        EmployeeMetrics metrics = metricsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Metrics record not found with id: " + id));
        metricsRepository.delete(metrics);
    }
}
