package com.moriku.healthcare_file_backend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moriku.healthcare_file_backend.dto.report.ReportCreateRequest;
import com.moriku.healthcare_file_backend.dto.report.ReportUpdateRequest;
import com.moriku.healthcare_file_backend.model.*;
import com.moriku.healthcare_file_backend.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ReportIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeProfileRepository employeeProfileRepository;

    @Autowired
    private ClientProfileRepository clientProfileRepository;

    @Autowired
    private CarePlanRepository carePlanRepository;

    @Autowired
    private CareTeamRepository careTeamRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Test
    void mockMvcLoads() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    @WithMockUser(username = "employee@test.local", authorities = {"EMPLOYEE"})    void employeeCanCreateReportForClientProfile() throws Exception {
        CareTeam careTeam = saveCareTeam();
        User employeeUser = saveEmployeeUser("employee@test.local");
        saveEmployeeProfile(employeeUser, careTeam);
        ClientProfile clientProfile = saveClientProfile(careTeam);
        CarePlan carePlan = saveCarePlan(clientProfile);

        EmployeeProfile savedEmployeeProfile = employeeProfileRepository.findById(employeeUser.getId())
            .orElseThrow();

        ClientProfile savedClientProfile = clientProfileRepository.findById(clientProfile.getId())
            .orElseThrow();

        assertThat(savedEmployeeProfile.getCareTeam()).isNotNull();
        assertThat(savedClientProfile.getCareTeam()).isNotNull();
        assertThat(savedEmployeeProfile.getCareTeam().getId()).isEqualTo(savedClientProfile.getCareTeam().getId());

        ReportCreateRequest request = new ReportCreateRequest();
        request.setCarePlanId(carePlan.getId());
        request.setTitle("Test");
        request.setText("Text");

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/reports")
                .contentType(APPLICATION_JSON)
                .content(json))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "employee2@test.local", authorities = {"EMPLOYEE"})
    void employeeCannotUpdateReportTheyDoNotOwn() throws Exception {
        CareTeam careTeam = saveCareTeam();

        User employee1User = saveEmployeeUser("employee1@test.local");
        EmployeeProfile employee1Profile = saveEmployeeProfile(employee1User, careTeam);

        User employee2User = saveEmployeeUser("employee2@test.local");
        saveEmployeeProfile(employee2User, careTeam);

        ClientProfile clientProfile = saveClientProfile(careTeam);
        CarePlan carePlan = saveCarePlan(clientProfile);

        Report report = saveReport("Original title", "Original text", carePlan, employee1Profile);

        ReportUpdateRequest request = new ReportUpdateRequest();
        request.setTitle("Updated title");
        request.setText("Updated text");

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/reports/{id}", report.getId())
                .contentType(APPLICATION_JSON)
                .content(json))
            .andDo(print())
            .andExpect(status().isForbidden());

        Report unchangedReport = reportRepository.findById(report.getId())
            .orElseThrow();

        assertThat(unchangedReport.getTitle()).isEqualTo("Original title");
        assertThat(unchangedReport.getText()).isEqualTo("Original text");
        assertThat(unchangedReport.getAuthor().getId()).isEqualTo(employee1Profile.getId());
    }

    private Role getOrCreateEmployeeRole() {
        return roleRepository.findByName("EMPLOYEE")
            .orElseGet(() -> roleRepository.save(new Role("EMPLOYEE")));
    }

    private User saveEmployeeUser(String email) {
        User user = new User(email, "password", getOrCreateEmployeeRole());
        return userRepository.save(user);
    }

    private CareTeam saveCareTeam() {
        CareTeam careTeam = new CareTeam(
            "Team Alpha",
            "0612345678",
            "team.alpha@test.local"
        );
        return careTeamRepository.save(careTeam);
    }

    private EmployeeProfile saveEmployeeProfile(User user, CareTeam careTeam) {
        EmployeeProfile employeeProfile = new EmployeeProfile(user);
        employeeProfile.setFirstName("Emma");
        employeeProfile.setLastName("Employee");
        employeeProfile.setCareTeam(careTeam);
        return employeeProfileRepository.save(employeeProfile);
    }

    private ClientProfile saveClientProfile(CareTeam careTeam) {
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setBsn("999991234");
        clientProfile.setFirstName("Clara");
        clientProfile.setLastName("Client");
        clientProfile.setBirthDate(java.time.LocalDate.of(1990, 1, 1));
        clientProfile.setSex(Sex.FEMALE);
        clientProfile.setActive(true);
        clientProfile.setCareTeam(careTeam);
        return clientProfileRepository.save(clientProfile);
    }

    private CarePlan saveCarePlan(ClientProfile clientProfile) {
        CarePlan carePlan = new CarePlan(clientProfile);
        carePlan.setNotes("notes");
        carePlan.setMedicalHistory("history");
        return carePlanRepository.save(carePlan);
    }

    private Report saveReport(String title, String text, CarePlan carePlan, EmployeeProfile author) {
        Report report = new Report(title, text, carePlan, author);
        return reportRepository.save(report);
    }
}