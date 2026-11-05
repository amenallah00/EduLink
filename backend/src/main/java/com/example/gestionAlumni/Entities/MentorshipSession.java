package com.example.gestionAlumni.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class MentorshipSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    LocalDateTime sessionDate;
    String topic;
    @Column(length = 1000)
    String summary;
    @Column(length = 1000)
    String nextSteps;

    int duration; // duration in minutes

    @ManyToOne
    @JoinColumn(name = "mentorship_request_id")
    MentorshipRequest mentorshipRequest;
}
