package com.example.gestionAlumni.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String company;
    String position;
    LocalDate startDate;
    LocalDate endDate;
    @Column(length = 1000)
    String description;

    @ManyToOne
    @JoinColumn(name = "alumni_id")
    Alumni alumni;
}
