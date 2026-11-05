package com.example.gestionAlumni.Repos;


import com.example.gestionAlumni.Entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,Long> {
    List<Application> findByAlumni_Id(Long alumniId);

    List<Application> findByStudent_Id(Long studentId);

    List<Application> findByStatus(Application.ApplicationStatus status);

    List<Application> findByAlumni_IdAndStatus(Long alumniId, Application.ApplicationStatus status);

    List<Application> findTop5ByAlumni_IdOrderBySubmissionDateDesc(Long alumniId);


}