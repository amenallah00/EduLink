package com.example.gestionAlumni.Entities;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "offers")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
  

    @OneToOne
    @JoinColumn(name = "mentorship_request_id")
    private MentorshipRequest mentorshipRequest;
    
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
    private String type; // "internship" or "job"
    private String duration;
    private String company;
    private String position;
    private Long proposedSalary;

   

    private Boolean status = true;

    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

   
}