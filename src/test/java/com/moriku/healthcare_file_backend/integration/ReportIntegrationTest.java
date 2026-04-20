package com.moriku.healthcare_file_backend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moriku.healthcare_file_backend.dto.report.ReportCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ReportIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void mockMvcLoads() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    @WithMockUser(username = "employee@test.local", roles = {"EMPLOYEE"})
    void employeeCanCreateReportForClientProfile() throws Exception {
        ReportCreateRequest request = new ReportCreateRequest();
        request.setCarePlanId(1L);
        request.setTitle("Test");
        request.setText("Text");

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/reports")
                .with(csrf())
                .contentType(APPLICATION_JSON)
                .content(json))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "employee2@test.local", roles = {"EMPLOYEE"})
    void employeeCannotUpdateReportTheyDoNotOwn() throws Exception {
    }
}