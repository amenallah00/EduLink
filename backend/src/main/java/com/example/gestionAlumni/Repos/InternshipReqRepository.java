package com.example.gestionAlumni.Repos;

import com.example.gestionAlumni.Entities.InternshipRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InternshipReqRepository extends JpaRepository<InternshipRequest,Long> {

    List<InternshipRequest> findBySender_Id(Long studentId);

    List<InternshipRequest> findByReceiver_Id(Long alumniId);

    List<InternshipRequest> findByType(InternshipRequest.InternshipType type);

    List<InternshipRequest> findByStatus(InternshipRequest.RequestStatus status);

    List<InternshipRequest> findByReceiver_IdAndStatus(Long alumniId, InternshipRequest.RequestStatus status);

    List<InternshipRequest> findBySubmissionDateAfter(LocalDateTime date);

    long countBySender_Id(Long studentId);

    long countByReceiver_IdAndStatus(Long alumniId, InternshipRequest.RequestStatus status);
}