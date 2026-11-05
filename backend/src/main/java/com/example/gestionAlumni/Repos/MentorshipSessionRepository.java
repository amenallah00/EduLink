package com.example.gestionAlumni.Repos;

import com.example.gestionAlumni.Entities.MentorshipSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MentorshipSessionRepository extends JpaRepository<MentorshipSession, Long> {
    List<MentorshipSession> findByMentorshipRequestId(Long mentorshipRequestId);

    @Query("SELECT ms FROM MentorshipSession ms WHERE ms.mentorshipRequest.proposer.id = :proposerId")
    List<MentorshipSession> findByMentorshipRequestProposerId(@Param("proposerId") Long proposerId);
}
