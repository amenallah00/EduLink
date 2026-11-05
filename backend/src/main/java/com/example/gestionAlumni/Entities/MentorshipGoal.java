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
public class MentorshipGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 500)
    String description;

    String status; // NOT_STARTED, IN_PROGRESS, COMPLETED

    @ManyToOne
    @JoinColumn(name = "mentorship_request_id")
    MentorshipRequest mentorshipRequest;
}
