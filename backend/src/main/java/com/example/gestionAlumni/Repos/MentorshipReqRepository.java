package com.example.gestionAlumni.Repos;

import com.example.gestionAlumni.Entities.MentorshipRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentorshipReqRepository extends JpaRepository<MentorshipRequest,Long> {

    List<MentorshipRequest> findByProposer_Id(Long alumniId);

    List<MentorshipRequest> findByStudent_Id(Long studentId);

    List<MentorshipRequest> findByStatus(String status);

    @Query("SELECT m FROM MentorshipRequest m WHERE m.generatedOffer IS NOT NULL")
    List<MentorshipRequest> findWithGeneratedOffers();

    @Query("SELECT m FROM MentorshipRequest m WHERE m.generatedOffer IS NULL")
    List<MentorshipRequest> findWithoutGeneratedOffers();

    long countByProposer_Id(Long alumniId);
}