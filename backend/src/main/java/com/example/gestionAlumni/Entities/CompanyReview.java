package com.example.gestionAlumni.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class CompanyReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String companyName;
    int rating; // 1-5
    @Column(length = 1000)
    String culture;
    @Column(length = 1000)
    String recruitmentProcess;
    @Column(length = 1000)
    String interviewTips;
    @Column(length = 1000)
    String missions;
    @Column(length = 1000)
    String workAtmosphere;
    @Column(length = 1000)
    String growthOpportunities;
    @Column(length = 1000)
    String pros;
    @Column(length = 1000)
    String cons;

    @ManyToOne
    @JoinColumn(name = "alumni_id")
    Alumni author;
}
