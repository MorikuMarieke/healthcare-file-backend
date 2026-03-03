package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.common.PageResponse;
import com.moriku.healthcare_file_backend.dto.report.ReportCreateRequest;
import com.moriku.healthcare_file_backend.dto.report.ReportResponse;
import com.moriku.healthcare_file_backend.dto.report.ReportUpdateRequest;
import com.moriku.healthcare_file_backend.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<ReportResponse> create(
        @Valid @RequestBody ReportCreateRequest request
    ) {
        ReportResponse response = reportService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getById(id));
    }

    @GetMapping("/care-plans/{carePlanId}")
    public ResponseEntity<List<ReportResponse>> getByCarePlan(@PathVariable Long carePlanId) {
        return ResponseEntity.ok(reportService.getByCarePlan(carePlanId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportResponse> update(@PathVariable Long id, @RequestBody ReportUpdateRequest request) {
        return ResponseEntity.ok(reportService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reportService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<ReportResponse>> getOverview(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "25") int size
    ) {
        return ResponseEntity.ok(reportService.getOverview(page, size));
    }
}