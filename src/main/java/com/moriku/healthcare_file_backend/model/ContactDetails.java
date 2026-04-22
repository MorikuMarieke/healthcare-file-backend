package com.moriku.healthcare_file_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "contact_details")
public class ContactDetails {

    @Id
    private Long id; // same as client_profile id

    @OneToOne
    @MapsId
    @JoinColumn(name = "client_profile_id")
    private ClientProfile clientProfile;

    @Column
    private String email;

    @Column
    private String primaryPhoneNumber;

    @Column
    private String secondaryPhoneNumber;

    @Column
    private String primaryPhysician;

    @Column
    private String firstEmergencyContactName;

    @Column
    private String firstEmergencyContactEmail;

    @Column
    private String firstEmergencyContactPhoneNumber;

    @Column(length = 500)
    private String address;

    public ContactDetails() {
    }

    public Long getId() {
        return id;
    }

    public ClientProfile getClientProfile() {
        return clientProfile;
    }

    public void setClientProfile(ClientProfile clientProfile) {
        this.clientProfile = clientProfile;
    }

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