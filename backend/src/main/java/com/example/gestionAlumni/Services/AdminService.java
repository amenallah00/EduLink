package com.example.gestionAlumni.Services;
import com.example.gestionAlumni.DTO.AlumniAdminSummaryDTO;
import com.example.gestionAlumni.DTO.StudentAdminSummaryDTO;
import com.example.gestionAlumni.Entities.Administrator;
import com.example.gestionAlumni.Entities.Alumni;
import com.example.gestionAlumni.Entities.Student;
import com.example.gestionAlumni.Repos.AdminRepository;
import com.example.gestionAlumni.Repos.AlumniRepository;
import com.example.gestionAlumni.Repos.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminrepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AlumniRepository alumniRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailService emailService;

    public Alumni approveAlumni(Long alumniId) {
        Alumni alumni = alumniRepository.findById(alumniId)
                .orElseThrow(() -> new RuntimeException("Alumni not found"));

        alumni.setVerified(true);
        alumniRepository.save(alumni);

        // Send acceptance email
        emailService.sendEmail(
                alumni.getEmail(),
                "Your Alumni Account is Approved",
                "Congratulations! Your alumni account has been approved. You can now log in."
        );

        return alumni;
    }

    public void rejectAlumni(Long alumniId) {
        Alumni alumni = alumniRepository.findById(alumniId)
                .orElseThrow(() -> new RuntimeException("Alumni not found"));

        alumniRepository.delete(alumni);
        emailService.sendEmail(
                alumni.getEmail(),
                "Your Alumni Account was Rejected",
                "We're sorry, but your alumni account request has been rejected. Please contact admin for details."
        );
    }

    public Administrator authenticate(String email, String rawPassword) {
        Administrator admin = adminrepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email"));

        if (!passwordEncoder.matches(rawPassword, admin.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!admin.isActive()) {
            throw new RuntimeException("Account is inactive.");
        }

        return admin;
    }

    public List<AlumniAdminSummaryDTO> listPendingAlumni() {
        return alumniRepository.findByVerifiedFalse().stream()
                .map(this::toAlumniSummaryDto)
                .collect(Collectors.toList());
    }

    public List<AlumniAdminSummaryDTO> listVerifiedAlumni() {
        return alumniRepository.findByVerifiedTrue().stream()
                .map(this::toAlumniSummaryDto)
                .collect(Collectors.toList());
    }

    public List<StudentAdminSummaryDTO> listAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::toStudentSummaryDto)
                .collect(Collectors.toList());
    }

    private AlumniAdminSummaryDTO toAlumniSummaryDto(Alumni a) {
        return new AlumniAdminSummaryDTO(
                a.getId(),
                a.getFirstName(),
                a.getLastName(),
                a.getEmail(),
                a.getGraduationYear(),
                a.getSpeciality(),
                a.isVerified(),
                a.getAvatarUrl()
        );
    }

    private StudentAdminSummaryDTO toStudentSummaryDto(Student s) {
        return new StudentAdminSummaryDTO(
                s.getId(),
                s.getFirstName(),
                s.getLastName(),
                s.getEmail(),
                s.getSpeciality(),
                s.getPredictedGradYear(),
                s.getAvatarUrl()
        );
    }
}
