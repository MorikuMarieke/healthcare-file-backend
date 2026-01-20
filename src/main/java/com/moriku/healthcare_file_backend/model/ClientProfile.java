package com.moriku.healthcare_file_backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "client_profiles")
public class ClientProfile {

    @Id
    private Long id; // same as user id

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private Boolean isActive;
    private LocalDate birthDate;
//    private ContactDetails contactDetails; // TODO: implement later when it's time to add the model
//    private Employee primaryCaregiver; // TODO: implement later when it's time to add the model


    public ClientProfile(User user, Boolean isActive, LocalDate birthDate) {
        this.user = user;
        this.isActive = isActive;
        this.birthDate = birthDate;
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}