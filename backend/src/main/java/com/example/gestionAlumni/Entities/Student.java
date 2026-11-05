package com.example.gestionAlumni.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@DiscriminatorValue("etudiant")
public class Student extends User{

    Float average;

    String speciality;

    int predictedGradYear;
    
    @JsonIgnore
    @Lob
    private byte[] document;

    private String documentName;

    private String searchType; 

    String education;
    String skills;
    String experiences;
    String projects;
    String interests;
    String availability;

    @JsonIgnore
    @OneToMany(mappedBy = "student")
    List<Application> applications;

    @JsonIgnore
    @OneToMany(mappedBy = "sender")
    List<InternshipRequest> internshipRequests;

}