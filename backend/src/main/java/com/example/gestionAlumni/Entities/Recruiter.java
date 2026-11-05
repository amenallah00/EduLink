package com.example.gestionAlumni.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@DiscriminatorValue("recruiter")
public class Recruiter extends User {

    String companyName;
    String sector;
    
    @Lob
    byte[] logo;
    
    String presentation;
    String contact;
    String location;

    @JsonIgnore
    @OneToMany(mappedBy = "creator")
    List<Offer> offers;
}
