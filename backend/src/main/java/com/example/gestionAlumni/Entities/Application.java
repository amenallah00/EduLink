package com.example.gestionAlumni.Entities;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false, updatable = false)
	    private LocalDateTime submissionDate = LocalDateTime.now();

	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private ApplicationStatus status = ApplicationStatus.PENDING;
		@ManyToOne
		@JoinColumn(name="alumni_id")
		private Alumni alumni;
        /*
	    // Méthode pour soumettre une nouvelle application
	    public static Application submit() {
	        return Application.builder()
	                .status(ApplicationStatus.PENDING)
	                .build();
	    }
         */
	    // Méthode pour mettre à jour le statut
	    public void updateStatus(ApplicationStatus newStatus) {
	        this.status = newStatus;
	    }

	    // Enum des statuts possibles
	    public enum ApplicationStatus {
	        PENDING,
	        ACCEPTED,
	        REJECTED
	    }
	    // Relation optionnelle avec un étudiant (exemple d'extension)
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "student_id")
	    private Student student;

}