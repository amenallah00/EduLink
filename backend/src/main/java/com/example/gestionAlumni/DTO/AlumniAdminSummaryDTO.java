package com.example.gestionAlumni.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlumniAdminSummaryDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer graduationYear;
    private String speciality;
    private boolean verified;
    private String avatarUrl;
}
