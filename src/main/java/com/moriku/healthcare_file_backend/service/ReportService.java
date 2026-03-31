package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.common.PageResponse;
import com.moriku.healthcare_file_backend.dto.report.ReportCreateRequest;
import com.moriku.healthcare_file_backend.dto.report.ReportResponse;
import com.moriku.healthcare_file_backend.dto.report.ReportUpdateRequest;
import com.moriku.healthcare_file_backend.mapper.ReportMapper;
import com.moriku.healthcare_file_backend.model.CarePlan;
import com.moriku.healthcare_file_backend.model.ClientProfile;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;
import com.moriku.healthcare_file_backend.model.Report;
import com.moriku.healthcare_file_backend.model.User;
import com.moriku.healthcare_file_backend.repository.CarePlanRepository;
import com.moriku.healthcare_file_backend.repository.ClientProfileRepository;
import com.moriku.healthcare_file_backend.repository.ReportRepository;
import com.moriku.healthcare_file_backend.security.SecurityContextService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final CarePlanRepository carePlanRepository;
    private final ClientProfileRepository clientProfileRepository;
    private final SecurityContextService securityContextService;

    public ReportService(
        ReportRepository reportRepository,
        CarePlanRepository carePlanRepository,
        ClientProfileRepository clientProfileRepository,
        SecurityContextService securityContextService
    ) {
        this.reportRepository = reportRepository;
        this.carePlanRepository = carePlanRepository;
        this.clientProfileRepository = clientProfileRepository;
        this.securityContextService = securityContextService;
    }

    // =====================================================
    // STAFF
    // =====================================================

    @Transactional
    public ReportResponse create(ReportCreateRequest request) {
        EmployeeProfile author = securityContextService.getCurrentEmployeeProfileOrThrow();

        CarePlan carePlan = carePlanRepository.findById(request.getCarePlanId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CarePlan not found"));

        ClientProfile clientProfile = carePlan.getClientProfile();

        securityContextService.assertCurrentEmployeeHasAccessToClientForWriteOrThrow(clientProfile);

        Report report = new Report(request.getTitle(), request.getText(), carePlan, author);
        reportRepository.save(report);

        return ReportMapper.toResponse(report);
    }

    @Transactional
    public ReportResponse update(Long reportId, ReportUpdateRequest request) {
        EmployeeProfile currentEmployee = securityContextService.getCurrentEmployeeProfileOrThrow();

        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found"));

        assertCurrentEmployeeIsAuthorOrThrow(currentEmployee, report);

        report.setTitle(request.getTitle());
        report.setText(request.getText());

        return ReportMapper.toResponse(report);
    }

    public ReportResponse getById(Long reportId) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found"));

        return ReportMapper.toResponse(report);
    }

    public List<ReportResponse> getByCarePlan(Long carePlanId) {
        return reportRepository.findByCarePlanIdOrderByCreatedAtDesc(carePlanId)
            .stream()
            .map(ReportMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long reportId) {
        EmployeeProfile currentEmployee = securityContextService.getCurrentEmployeeProfileOrThrow();

        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found"));

        assertCurrentEmployeeIsAuthorOrThrow(currentEmployee, report);

        reportRepository.delete(report);
    }

    public PageResponse<ReportResponse> getOverview(int page, int size) {
        int safeSize = Math.min(Math.max(size, 1), 25);

        Pageable pageable = PageRequest.of(
            page,
            safeSize,
            Sort.by("createdAt").descending()
        );

        Page<Report> result = reportRepository.findAll(pageable);

        List<ReportResponse> content = result.getContent()
            .stream()
            .map(ReportMapper::toResponse)
            .toList();

        return new PageResponse<>(
            content,
            result.getNumber(),
            result.getSize(),
            result.getTotalElements(),
            result.getTotalPages(),
            result.isLast()
        );
    }

    // =====================================================
    // /me (CLIENT read-only)
    // =====================================================

    public List<ReportResponse> getMyReports() {
        CarePlan myCarePlan = getMyCarePlan();
        return getByCarePlan(myCarePlan.getId());
    }

    public ReportResponse getMyReportById(Long reportId) {
        CarePlan myCarePlan = getMyCarePlan();

        Report report = reportRepository.findByIdAndCarePlanId(reportId, myCarePlan.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found"));

        return ReportMapper.toResponse(report);
    }

    // =====================================================
    // helpers
    // =====================================================

    private void assertCurrentEmployeeIsAuthorOrThrow(EmployeeProfile employee, Report report) {
        if (!report.getAuthor().getId().equals(employee.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the author of this report");
        }
    }

    private CarePlan getMyCarePlan() {
        User user = securityContextService.getCurrentUserOrThrow();

        ClientProfile clientProfile = clientProfileRepository.findByUserId(user.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ClientProfile not found"));

        return carePlanRepository.findByClientProfileId(clientProfile.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CarePlan not found"));
    }
}