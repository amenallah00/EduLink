package com.example.gestionAlumni.Controllers;

import com.example.gestionAlumni.Entities.MentorshipEvaluation;
import com.example.gestionAlumni.Entities.MentorshipGoal;
import com.example.gestionAlumni.Entities.MentorshipRequest;
import com.example.gestionAlumni.Entities.MentorshipSession;
import com.example.gestionAlumni.Repos.MentorshipEvaluationRepository;
import com.example.gestionAlumni.Repos.MentorshipGoalRepository;
import com.example.gestionAlumni.Repos.MentorshipReqRepository;
import com.example.gestionAlumni.Repos.MentorshipSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mentorship")
public class MentorshipController {

    @Autowired
    private MentorshipReqRepository mentorshipReqRepository;

    @Autowired
    private MentorshipSessionRepository mentorshipSessionRepository;

    @Autowired
    private MentorshipEvaluationRepository mentorshipEvaluationRepository;

    @Autowired
    private MentorshipGoalRepository mentorshipGoalRepository;

    // ─── Requests ───────────────────────────────────────────

    @PostMapping("/requests")
    public MentorshipRequest submitRequest(@RequestBody MentorshipRequest request) {
        request.setStatus("PENDING");
        return mentorshipReqRepository.save(request);
    }

    @GetMapping("/requests/{id}")
    public ResponseEntity<MentorshipRequest> getRequest(@PathVariable Long id) {
        return mentorshipReqRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/requests/student/{studentId}")
    public List<MentorshipRequest> getStudentRequests(@PathVariable Long studentId) {
        return mentorshipReqRepository.findByStudent_Id(studentId);
    }

    @GetMapping("/requests/alumni/{alumniId}")
    public List<MentorshipRequest> getAlumniRequests(@PathVariable Long alumniId) {
        return mentorshipReqRepository.findByProposer_Id(alumniId);
    }

    @PutMapping("/requests/{id}/status")
    public MentorshipRequest updateStatus(@PathVariable Long id, @RequestParam String status) {
        MentorshipRequest request = mentorshipReqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentorship request not found"));
        request.setStatus(status);
        return mentorshipReqRepository.save(request);
    }

    @PutMapping("/requests/{id}/cancel")
    public MentorshipRequest cancelRequest(@PathVariable Long id) {
        MentorshipRequest request = mentorshipReqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentorship request not found"));
        request.setStatus("CANCELLED");
        return mentorshipReqRepository.save(request);
    }

    @PutMapping("/requests/{id}/complete")
    public MentorshipRequest completeRequest(@PathVariable Long id) {
        MentorshipRequest request = mentorshipReqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentorship request not found"));
        request.setStatus("COMPLETED");
        return mentorshipReqRepository.save(request);
    }

    // ─── Sessions ───────────────────────────────────────────

    @PostMapping("/sessions")
    public MentorshipSession addSession(@RequestBody MentorshipSession session) {
        return mentorshipSessionRepository.save(session);
    }

    @GetMapping("/requests/{id}/sessions")
    public List<MentorshipSession> getSessions(@PathVariable Long id) {
        return mentorshipSessionRepository.findByMentorshipRequestId(id);
    }

    // ─── Goals ──────────────────────────────────────────────

    @PostMapping("/goals")
    public MentorshipGoal createGoal(@RequestBody MentorshipGoal goal) {
        if (goal.getStatus() == null) {
            goal.setStatus("NOT_STARTED");
        }
        return mentorshipGoalRepository.save(goal);
    }

    @GetMapping("/requests/{id}/goals")
    public List<MentorshipGoal> getGoals(@PathVariable Long id) {
        return mentorshipGoalRepository.findByMentorshipRequestId(id);
    }

    @PutMapping("/goals/{id}/status")
    public ResponseEntity<MentorshipGoal> updateGoalStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return mentorshipGoalRepository.findById(id)
                .map(goal -> {
                    goal.setStatus(body.get("status"));
                    return ResponseEntity.ok(mentorshipGoalRepository.save(goal));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ─── Evaluations ────────────────────────────────────────

    @PostMapping("/evaluations")
    public MentorshipEvaluation submitEvaluation(@RequestBody MentorshipEvaluation evaluation) {
        return mentorshipEvaluationRepository.save(evaluation);
    }

    @GetMapping("/requests/{id}/evaluations")
    public List<MentorshipEvaluation> getEvaluations(@PathVariable Long id) {
        return mentorshipEvaluationRepository.findByMentorshipRequestId(id);
    }
}
