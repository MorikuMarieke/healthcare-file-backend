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
}
