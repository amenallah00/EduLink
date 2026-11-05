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
public class MentorshipEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    int rating; // 1-5
    @Column(length = 1000)
    String comment;
    String evaluatorRole; // MENTOR or MENTEE

    @ManyToOne
    @JoinColumn(name = "mentorship_request_id")
    MentorshipRequest mentorshipRequest;
}
