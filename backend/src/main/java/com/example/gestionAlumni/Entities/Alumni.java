package com.example.gestionAlumni.Entities;



import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@DiscriminatorValue("alumni")
public class Alumni extends User{

    String currentCompany;

    String currentJob;

    Long salary;

    String speciality;

    boolean openToMentoring;
    boolean willingToRefer;
    boolean openToCareerAdvice;
    
    String academicPath;
    String professionalPath;
    String expertise;

    @ElementCollection
    List<String> skills;

    String sector;
    String availabilityDetails;

    @Transient
    Double rating; // For displaying computed rating from evaluations

    @Lob
    @Column(name = "document", columnDefinition = "LONGBLOB")
    byte[] document;

    String documentName;

    @OneToMany(mappedBy = "alumni", cascade = CascadeType.ALL)
    List<Experience> experiences;



    @JsonIgnore
    @OneToMany
    List<Application> applications;

    @JsonIgnore
    @OneToMany
    List<MentorshipRequest> mentorshipRequests;

    @JsonIgnore
    @OneToMany
    List<InternshipRequest> internshipRequestsReceived;
    
    @JsonIgnore
    @OneToMany(mappedBy = "creator")
    List<Offer> offers;




}