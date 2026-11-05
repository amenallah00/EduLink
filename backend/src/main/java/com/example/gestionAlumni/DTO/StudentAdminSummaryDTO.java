package com.example.gestionAlumni.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAdminSummaryDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String speciality;
    private Integer predictedGradYear;
    private String avatarUrl;
}
