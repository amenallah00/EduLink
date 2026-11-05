package com.example.gestionAlumni.Entities;




import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@DiscriminatorValue("admin")
public class Administrator extends User {
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "admin_unverified_alumni",
            joinColumns = @JoinColumn(name = "admin_id"),
            inverseJoinColumns = @JoinColumn(name = "alumni_id")
    )
    private List<Alumni> unverifiedAlumni = new ArrayList<>();

    public void addUnverifiedAlumni(Alumni alumni) {
        this.unverifiedAlumni.add(alumni);
    }

    public void removeUnverifiedAlumni(Alumni alumni) {
        this.unverifiedAlumni.remove(alumni);
    }

    public void verifyAlumni(Alumni alumni, String applicationBaseUrl) {
        if (!unverifiedAlumni.contains(alumni)) {
            throw new IllegalArgumentException("Alumni not found in unverified list.");
        }

        // Generate a unique verification token
        String verificationToken = UUID.randomUUID().toString();
        alumni.setVerificationToken(verificationToken);

        // Send verification email
        sendVerificationEmail(alumni.getEmail(), verificationToken, applicationBaseUrl);

        // Move alumni to verified list (optional)
        unverifiedAlumni.remove(alumni);
    }

    // Helper method to send email
    private void sendVerificationEmail(String alumniEmail, String token, String baseUrl) {
        String subject = "Verify Your Alumni Account";
        String verificationLink = baseUrl + "/verify-account?token=" + token;
        String body = "Click the link below to verify your account:\n" + verificationLink;

        // Email sending should be handled in a Spring service layer, not in JPA entities.
        // This method currently builds the verification content only.
    }

}