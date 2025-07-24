package org.exemple.virementservice.web;

import org.exemple.virementservice.dtos.PerformanceDTO;
import org.exemple.virementservice.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/performances")
public class PerformanceController {

    private final PerformanceService performanceService;

    @Autowired
    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @PostMapping
    public ResponseEntity<PerformanceDTO> createPerformance(@RequestBody PerformanceDTO performanceDTO) {
        return new ResponseEntity<>(performanceService.createPerformance(performanceDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerformanceDTO> getPerformanceById(@PathVariable Long id) {
        return ResponseEntity.ok(performanceService.getPerformanceById(id));
    }

    @GetMapping
    public ResponseEntity<List<PerformanceDTO>> getAllPerformances() {
        return ResponseEntity.ok(performanceService.getAllPerformances());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<PerformanceDTO>> getPerformancesByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(performanceService.getPerformancesByEmployeeId(employeeId));
    }

    @GetMapping("/employee/{employeeId}/daterange")
    public ResponseEntity<List<PerformanceDTO>> getPerformancesByEmployeeIdAndDateRange(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(performanceService.getPerformancesByEmployeeIdAndDateRange(employeeId, startDate, endDate));
    }

    @GetMapping("/employee/{employeeId}/average-rating")
    public ResponseEntity<Double> getAverageRatingByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(performanceService.getAverageRatingByEmployeeId(employeeId));
    }

    @GetMapping("/employee/{employeeId}/average-rating/daterange")
    public ResponseEntity<Double> getAverageRatingByEmployeeIdAndDateRange(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(performanceService.getAverageRatingByEmployeeIdAndDateRange(employeeId, startDate, endDate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerformanceDTO> updatePerformance(
            @PathVariable Long id, @RequestBody PerformanceDTO performanceDTO) {
        return ResponseEntity.ok(performanceService.updatePerformance(id, performanceDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerformance(@PathVariable Long id) {
        performanceService.deletePerformance(id);
        return ResponseEntity.noContent().build();
    }
}
   