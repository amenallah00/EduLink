package com.example.gestionAlumni.Entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "internship_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class InternshipRequest {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InternshipType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    @Column(nullable = false, updatable = false)
    private LocalDateTime submissionDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name="student_id")
    private Student sender;

    @ManyToOne
    @JoinColumn(name="alumni_id")
    private Alumni receiver;

    // Enum pour le type de stage
    public enum InternshipType {
        SUMMER_INTERNSHIP, 
        FINAL_YEAR_PROJECT
    }
    // Enum pour le statut de la demande
    public enum RequestStatus {
        PENDING,
        APPROVED,
        REJECTED // Ajout recommandé pour une gestion complète
    }
    /*
 // Méthode pour soumettre une demande
    public static InternshipRequest submitRequest(String description, InternshipType type) {
        return InternshipRequest.builder()
                .description(description)
                .type(type)
                .status(RequestStatus.PENDING)
                .build();
    }
    */
}