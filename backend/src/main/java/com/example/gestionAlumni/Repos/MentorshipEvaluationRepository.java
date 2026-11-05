package com.example.gestionAlumni.Repos;

import com.example.gestionAlumni.Entities.MentorshipEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MentorshipEvaluationRepository extends JpaRepository<MentorshipEvaluation, Long> {
    List<MentorshipEvaluation> findByMentorshipRequestId(Long mentorshipRequestId);

    @Query("SELECT me FROM MentorshipEvaluation me WHERE me.mentorshipRequest.proposer.id = :proposerId")
    List<MentorshipEvaluation> findByMentorshipRequestProposerId(@Param("proposerId") Long proposerId);
}
