package com.moriku.healthcare_file_backend.dto.client_profile;

import jakarta.validation.constraints.Email;

public class ContactDetailsPatchRequest {

    @Email
    private String email;

    private String primaryPhoneNumber;

    private String secondaryPhoneNumber;

    private String primaryPhysician;

    private String firstEmergencyContactName;

    @Email
    private String firstEmergencyContactEmail;

    private String firstEmergencyContactPhoneNumber;

    private String address;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrimaryPhoneNumber() {
        return primaryPhoneNumber;
    }

    public void setPrimaryPhoneNumber(String primaryPhoneNumber) {
        this.primaryPhoneNumber = primaryPhoneNumber;
    }

    public String getSecondaryPhoneNumber() {
        return secondaryPhoneNumber;
    }

    public void setSecondaryPhoneNumber(String secondaryPhoneNumber) {
        this.secondaryPhoneNumber = secondaryPhoneNumber;
    }

    public String getPrimaryPhysician() {
        return primaryPhysician;
    }

    public void setPrimaryPhysician(String primaryPhysician) {
        this.primaryPhysician = primaryPhysician;
    }

    public String getFirstEmergencyContactName() {
        return firstEmergencyContactName;
    }

    public void setFirstEmergencyContactName(String firstEmergencyContactName) {
        this.firstEmergencyContactName = firstEmergencyContactName;
    }

    public String getFirstEmergencyContactEmail() {
        return firstEmergencyContactEmail;
    }

    public void setFirstEmergencyContactEmail(String firstEmergencyContactEmail) {
        this.firstEmergencyContactEmail = firstEmergencyContactEmail;
    }

    public String getFirstEmergencyContactPhoneNumber() {
        return firstEmergencyContactPhoneNumber;
    }

    public void setFirstEmergencyContactPhoneNumber(String firstEmergencyContactPhoneNumber) {
        this.firstEmergencyContactPhoneNumber = firstEmergencyContactPhoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}