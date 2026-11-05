package com.example.gestionAlumni.Repos;

import com.example.gestionAlumni.Entities.MentorshipGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentorshipGoalRepository extends JpaRepository<MentorshipGoal, Long> {
    List<MentorshipGoal> findByMentorshipRequestId(Long mentorshipRequestId);
}
