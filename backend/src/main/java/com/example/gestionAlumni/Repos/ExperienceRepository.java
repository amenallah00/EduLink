package com.example.gestionAlumni.Repos;

import com.example.gestionAlumni.Entities.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findByAlumniId(Long alumniId);
}
