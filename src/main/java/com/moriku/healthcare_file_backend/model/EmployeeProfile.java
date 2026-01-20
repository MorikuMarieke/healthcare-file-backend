package com.moriku.healthcare_file_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "employee_profiles")
public class EmployeeProfile {

    @Id
    private Long id; // same as user id

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column (nullable = false)
    private Boolean isMainNurse;

    @Column (nullable = false)
    private String workPhoneNumber;

    @Column (nullable = false)
    private String personalPhoneNumber;

    @Column
    private String personalEmail;

    public EmployeeProfile() {}

    public EmployeeProfile(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getMainNurse() {
        return isMainNurse;
    }

    public void setMainNurse(Boolean mainNurse) {
        isMainNurse = mainNurse;
    }

    public String getWorkPhoneNumber() {
        return workPhoneNumber;
    }

    public void setWorkPhoneNumber(String workPhoneNumber) {
        this.workPhoneNumber = workPhoneNumber;
    }

    public String getPersonalPhoneNumber() {
        return personalPhoneNumber;
    }

    public void setPersonalPhoneNumber(String personalPhoneNumber) {
        this.personalPhoneNumber = personalPhoneNumber;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }
}

