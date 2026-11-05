package com.example.gestionAlumni.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter

@ToString

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class AlumniPreferencesDTO {
    private boolean openToMentoring;
    private boolean willingToRefer;
    private boolean openToCareerAdvice;
}
