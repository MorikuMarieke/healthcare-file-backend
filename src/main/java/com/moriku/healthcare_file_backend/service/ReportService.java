package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.report.ReportCreateRequest;
import com.moriku.healthcare_file_backend.dto.report.ReportResponse;
import com.moriku.healthcare_file_backend.dto.report.ReportUpdateRequest;
import com.moriku.healthcare_file_backend.mapper.ReportMapper;
import com.moriku.healthcare_file_backend.model.CarePlan;
import com.moriku.healthcare_file_backend.model.EmployeeProfile;
import com.moriku.healthcare_file_backend.model.ClientProfile;
import com.moriku.healthcare_file_backend.model.Report;
import com.moriku.healthcare_file_backend.model.User;
import com.moriku.healthcare_file_backend.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final CarePlanRepository carePlanRepository;
    private final UserRepository userRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final ClientProfileRepository clientProfileRepository;

    public ReportService(ReportRepository reportRepository,
                         CarePlanRepository carePlanRepository,
                         UserRepository userRepository,
                         EmployeeProfileRepository employeeProfileRepository,
                         ClientProfileRepository clientProfileRepository) {
        this.reportRepository = reportRepository;
        this.carePlanRepository = carePlanRepository;
        this.userRepository = userRepository;
        this.employeeProfileRepository = employeeProfileRepository;
        this.clientProfileRepository = clientProfileRepository;
    }

    @Transactional
    public ReportResponse create(ReportCreateRequest request) {
        EmployeeProfile author = getCurrentEmployeeProfile();

        CarePlan carePlan = carePlanRepository.findById(request.getCarePlanId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CarePlan not found"));

        Report report = new Report(request.getTitle(), request.getText(), carePlan, author);
        reportRepository.save(report);

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
    public ReportResponse update(Long reportId, ReportUpdateRequest request) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found"));

        report.setTitle(request.getTitle());
        report.setText(request.getText());

        return ReportMapper.toResponse(report);
    }

    @Transactional
    public void delete(Long reportId) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found"));

        reportRepository.delete(report);
    }

    // ---- /me (CLIENT read-only) ----

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

    // ---- helpers ----

    private EmployeeProfile getCurrentEmployeeProfile() {
        User user = getCurrentUser();

        return employeeProfileRepository.findByUser_Id(user.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "EmployeeProfile not found"));
    }

    private CarePlan getMyCarePlan() {
        User user = getCurrentUser();

        ClientProfile clientProfile = clientProfileRepository.findByUserId(user.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ClientProfile not found"));

        return carePlanRepository.findByClientProfileId(clientProfile.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CarePlan not found"));
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        String email = authentication.getName().trim().toLowerCase();

        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }
}