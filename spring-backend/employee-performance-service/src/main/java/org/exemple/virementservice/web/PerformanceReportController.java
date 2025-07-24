package org.exemple.virementservice.web;

import org.exemple.virementservice.dtos.PerformanceReportDTO;
import org.exemple.virementservice.service.PerformanceReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
public class PerformanceReportController {

    private final PerformanceReportService reportService;

    @Autowired
    public PerformanceReportController(PerformanceReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<PerformanceReportDTO> generateEmployeeReport(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        PerformanceReportDTO report = reportService.generatePerformanceReport(employeeId, startDate, endDate);
        return ResponseEntity.ok(report);
    }
}