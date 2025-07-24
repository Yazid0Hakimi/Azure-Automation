package org.exemple.virementservice.web;

import org.exemple.virementservice.dtos.EmployeeMetricsDTO;
import org.exemple.virementservice.service.EmployeeMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/metrics")
public class EmployeeMetricsController {

    private final EmployeeMetricsService metricsService;

    @Autowired
    public EmployeeMetricsController(EmployeeMetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @PostMapping
    public ResponseEntity<EmployeeMetricsDTO> createMetrics(@RequestBody EmployeeMetricsDTO metricsDTO) {
        return new ResponseEntity<>(metricsService.createMetrics(metricsDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeMetricsDTO> getMetricsById(@PathVariable Long id) {
        return ResponseEntity.ok(metricsService.getMetricsById(id));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeMetricsDTO>> getAllMetrics() {
        return ResponseEntity.ok(metricsService.getAllMetrics());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeMetricsDTO>> getMetricsByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(metricsService.getMetricsByEmployeeId(employeeId));
    }

    @GetMapping("/employee/{employeeId}/daterange")
    public ResponseEntity<List<EmployeeMetricsDTO>> getMetricsByEmployeeIdAndDateRange(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(metricsService.getMetricsByEmployeeIdAndDateRange(employeeId, startDate, endDate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeMetricsDTO> updateMetrics(
            @PathVariable Long id, @RequestBody EmployeeMetricsDTO metricsDTO) {
        return ResponseEntity.ok(metricsService.updateMetrics(id, metricsDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetrics(@PathVariable Long id) {
        metricsService.deleteMetrics(id);
        return ResponseEntity.noContent().build();
    }
}