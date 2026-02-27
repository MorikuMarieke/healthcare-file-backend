package com.moriku.healthcare_file_backend.dto.client_profile;

import com.moriku.healthcare_file_backend.model.Sex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class ClientProfileCreateRequest {

    @NotBlank
    @Pattern(regexp = "^\\d{9}$", message = "BSN must be exactly 9 digits")
    private String bsn;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    private Sex sex;

    @NotNull
    private LocalDate birthDate;

    public String getBsn() {
        return bsn;
    }

    public void setBsn(String bsn) {
        this.bsn = bsn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public @NotNull Sex getSex() {
        return sex;
    }

    public void setSex(@NotNull Sex sex) {
        this.sex = sex;
    }

    public @NotNull LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(@NotNull LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
