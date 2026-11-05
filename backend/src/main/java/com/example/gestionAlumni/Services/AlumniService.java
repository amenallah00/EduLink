package com.example.gestionAlumni.Services;
import com.example.gestionAlumni.DTO.AlumniPreferencesDTO;
import com.example.gestionAlumni.Entities.Alumni;
import com.example.gestionAlumni.Repos.AlumniRepository;
import com.example.gestionAlumni.Repos.MentorshipEvaluationRepository;
import com.example.gestionAlumni.Repos.MentorshipSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlumniService {
    @Autowired
    private AlumniRepository alumniRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private MentorshipEvaluationRepository mentorshipEvaluationRepository;
    @Autowired
    private MentorshipSessionRepository mentorshipSessionRepository;

    public Alumni signup(Alumni alumni) {
        if (alumniRepository.findByEmail(alumni.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        alumni.setPassword(passwordEncoder.encode(alumni.getPassword()));
        
        String token = UUID.randomUUID().toString();
        alumni.setVerificationToken(token);
        alumni.setVerified(false);

        Alumni savedAlumni = alumniRepository.save(alumni);
        emailService.sendVerificationEmail(savedAlumni.getEmail(), savedAlumni.getFirstName(), token, "alumni");
        
        return savedAlumni;
    }

    public Alumni authenticate(String email, String rawPassword) {
        Alumni alumni = alumniRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email"));

        if (!passwordEncoder.matches(rawPassword, alumni.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!alumni.isVerified()) {
            throw new RuntimeException("Account not verified. Check your email.");
        }

        return alumni;
    }

    public Alumni verifyAlumni(String token) {
        Alumni alumni = alumniRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        alumni.setVerified(true);
        alumni.setVerificationToken(null); // Clear token after verification
        return alumniRepository.save(alumni);
    }
   
    public Alumni updateJobTitle(Long alumniId, String newJobTitle) {
        Alumni alumni = alumniRepository.findById(alumniId)
                .orElseThrow(() -> new RuntimeException("Alumni not found"));
        alumni.setCurrentJob(newJobTitle);
        return alumniRepository.save(alumni);
    }

    public Alumni updateCompany(Long alumniId, String newCompany) {
        Alumni alumni = alumniRepository.findById(alumniId)
                .orElseThrow(() -> new RuntimeException("Alumni not found"));
        alumni.setCurrentCompany(newCompany);
        return alumniRepository.save(alumni);
    }

    public Alumni updatePreferences(Long id, AlumniPreferencesDTO preferencesDTO) {
        Alumni alumni = alumniRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumni not found"));

        alumni.setOpenToMentoring(preferencesDTO.isOpenToMentoring());
        alumni.setWillingToRefer(preferencesDTO.isWillingToRefer());
        alumni.setOpenToCareerAdvice(preferencesDTO.isOpenToCareerAdvice());

        return alumniRepository.save(alumni);
    }

    public Alumni findById(Long id) {
        return alumniRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumni not found"));
    }

    public Alumni save(Alumni alumni) {
        return alumniRepository.save(alumni);
    }

    public Alumni updateProfile(Long id, Alumni updatedData) {
        Alumni alumni = alumniRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumni not found"));
        
        if (updatedData.getFirstName() != null) alumni.setFirstName(updatedData.getFirstName());
        if (updatedData.getLastName() != null) alumni.setLastName(updatedData.getLastName());
        if (updatedData.getSpeciality() != null) alumni.setSpeciality(updatedData.getSpeciality());
        if (updatedData.getCurrentJob() != null) alumni.setCurrentJob(updatedData.getCurrentJob());
        if (updatedData.getCurrentCompany() != null) alumni.setCurrentCompany(updatedData.getCurrentCompany());
        if (updatedData.getGraduationYear() != null) alumni.setGraduationYear(updatedData.getGraduationYear());
        if (updatedData.getAcademicPath() != null) alumni.setAcademicPath(updatedData.getAcademicPath());
        if (updatedData.getProfessionalPath() != null) alumni.setProfessionalPath(updatedData.getProfessionalPath());
        if (updatedData.getExpertise() != null) alumni.setExpertise(updatedData.getExpertise());
        
        return alumniRepository.save(alumni);
    }

    public Alumni uploadResume(Long id, MultipartFile file) throws IOException {
        Alumni alumni = alumniRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumni not found"));
        
        if (file != null && !file.isEmpty()) {
            alumni.setDocument(file.getBytes());
            alumni.setDocumentName(file.getOriginalFilename());
        }
        
        return alumniRepository.save(alumni);
    }
    public List<Alumni> findAllVerified() {
        return alumniRepository.findByVerifiedTrue();
    }

    public List<Alumni> searchAlumni(String query, String sector, String company, Integer graduationYear, String skill) {
        return alumniRepository.searchDirectory(query, sector, company, graduationYear, skill);
    }

    public List<Alumni> searchMentors(String company, String job, String sector, String speciality, String skill) {
        return alumniRepository.searchMentors(company, job, sector, speciality, skill);
    }
    public Double getAverageRating(Long alumniId) {
        List<Integer> ratings = mentorshipEvaluationRepository
                .findByMentorshipRequestProposerId(alumniId)
                .stream()
                .map(evaluation -> evaluation.getRating())
                .collect(Collectors.toList());
        
        if (ratings.isEmpty()) return 0.0;
        return ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    public List<Alumni> getTopRatedMentors(int limit) {
        List<Alumni> mentors = alumniRepository.findByVerifiedTrueAndOpenToMentoringTrue();
        
        return mentors.stream()
                .map(mentor -> {
                    Double rating = getAverageRating(mentor.getId());
                    mentor.setRating(rating);
                    return mentor;
                })
                .sorted((a, b) -> Double.compare(b.getRating() != null ? b.getRating() : 0.0, 
                                                  a.getRating() != null ? a.getRating() : 0.0))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getMentorStats(Long alumniId) {
        Alumni alumni = alumniRepository.findById(alumniId)
                .orElseThrow(() -> new RuntimeException("Alumni not found"));
        
        int totalSessions = mentorshipSessionRepository.findByMentorshipRequestProposerId(alumniId).size();
        Double averageRating = getAverageRating(alumniId);
        int reviewCount = mentorshipEvaluationRepository.findByMentorshipRequestProposerId(alumniId).size();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("mentorId", alumniId);
        stats.put("mentorName", alumni.getFirstName() + " " + alumni.getLastName());
        stats.put("totalSessions", totalSessions);
        stats.put("averageRating", Math.round(averageRating * 100.0) / 100.0);
        stats.put("reviewCount", reviewCount);
        stats.put("isAvailable", alumni.isOpenToMentoring());
        stats.put("expertise", alumni.getExpertise());
        
        return stats;
    }
}
