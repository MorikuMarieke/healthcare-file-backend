package com.moriku.healthcare_file_backend.dto.client_profile;

public class ContactDetailsResponse {

    private Long clientProfileId;
    private String email;
    private String primaryPhoneNumber;
    private String secondaryPhoneNumber;
    private String primaryPhysician;
    private String firstEmergencyContactName;
    private String firstEmergencyContactEmail;
    private String firstEmergencyContactPhoneNumber;
    private String address;

    public ContactDetailsResponse(
        Long clientProfileId,
        String email,
        String primaryPhoneNumber,
        String secondaryPhoneNumber,
        String primaryPhysician,
        String firstEmergencyContactName,
        String firstEmergencyContactEmail,
        String firstEmergencyContactPhoneNumber,
        String address
    ) {
        this.clientProfileId = clientProfileId;
        this.email = email;
        this.primaryPhoneNumber = primaryPhoneNumber;
        this.secondaryPhoneNumber = secondaryPhoneNumber;
        this.primaryPhysician = primaryPhysician;
        this.firstEmergencyContactName = firstEmergencyContactName;
        this.firstEmergencyContactEmail = firstEmergencyContactEmail;
        this.firstEmergencyContactPhoneNumber = firstEmergencyContactPhoneNumber;
        this.address = address;
    }

    public Long getClientProfileId() {
        return clientProfileId;
    }

    public String getEmail() {
        return email;
    }

    public String getPrimaryPhoneNumber() {
        return primaryPhoneNumber;
    }

    public String getSecondaryPhoneNumber() {
        return secondaryPhoneNumber;
    }

    public String getPrimaryPhysician() {
        return primaryPhysician;
    }

    public String getFirstEmergencyContactName() {
        return firstEmergencyContactName;
    }

    public String getFirstEmergencyContactEmail() {
        return firstEmergencyContactEmail;
    }

    public String getFirstEmergencyContactPhoneNumber() {
        return firstEmergencyContactPhoneNumber;
    }

    public String getAddress() {
        return address;
    }
}