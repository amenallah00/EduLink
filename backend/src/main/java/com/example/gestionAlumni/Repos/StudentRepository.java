package com.example.gestionAlumni.Repos;


import com.example.gestionAlumni.Entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);
    Optional<Student> findByVerificationToken(String token);

    List<Student> findBySpeciality(String speciality);

    List<Student> findByPredictedGradYear(int predictedGradYear);

    List<Student> findByAverageGreaterThan(Float minAverage);
}