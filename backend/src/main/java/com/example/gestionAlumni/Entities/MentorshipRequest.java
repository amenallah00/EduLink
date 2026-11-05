package com.example.gestionAlumni.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "monitor_requests")
public class MentorshipRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String details;

    @Column(length = 2000)
    private String objectives;

    private String duration;
    private String status; // PENDING, ACCEPTED, REJECTED, COMPLETED, CANCELLED

    private String category; // Carrière, Technique, Réseau, Académique, Projet

    @Column(length = 2000)
    private String message;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonIgnoreProperties({"applications", "internshipRequests", "document", "password"})
    private Student student;

    @ManyToOne
    @JoinColumn(name = "alumni_id")
    @JsonIgnoreProperties({"experiences", "applications", "mentorshipRequests", "internshipRequestsReceived", "offers", "document", "password"})
    private Alumni proposer;

    @JsonIgnore
    @OneToOne(mappedBy = "mentorshipRequest")
    private Offer generatedOffer;

    @JsonIgnore
    @OneToMany(mappedBy = "mentorshipRequest", cascade = CascadeType.ALL)
    private List<MentorshipSession> sessions;

    @JsonIgnore
    @OneToMany(mappedBy = "mentorshipRequest", cascade = CascadeType.ALL)
    private List<MentorshipEvaluation> evaluations;

    @JsonIgnore
    @OneToMany(mappedBy = "mentorshipRequest", cascade = CascadeType.ALL)
    private List<MentorshipGoal> goals;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}