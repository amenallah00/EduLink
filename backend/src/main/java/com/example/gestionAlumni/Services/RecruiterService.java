package com.example.gestionAlumni.Services;

import com.example.gestionAlumni.Entities.Recruiter;
import com.example.gestionAlumni.Repos.RecruiterRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RecruiterService {

    RecruiterRepository recruiterRepository;
    PasswordEncoder passwordEncoder;
    EmailService emailService;

    public Recruiter signup(Recruiter recruiter) {
        if (recruiterRepository.findByEmail(recruiter.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }
        recruiter.setPassword(passwordEncoder.encode(recruiter.getPassword()));
        
        String token = UUID.randomUUID().toString();
        recruiter.setVerificationToken(token);
        recruiter.setVerified(false);
        recruiter.setActive(true);

        Recruiter savedRecruiter = recruiterRepository.save(recruiter);
        emailService.sendVerificationEmail(savedRecruiter.getEmail(), savedRecruiter.getFirstName(), token, "recruiter");
        return savedRecruiter;
    }

    public Recruiter authenticate(String email, String rawPassword) {
        Recruiter recruiter = recruiterRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email"));

        if (!passwordEncoder.matches(rawPassword, recruiter.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!recruiter.isVerified()) {
            throw new RuntimeException("Account not verified. Check your email.");
        }

        if (!recruiter.isActive()) {
            throw new RuntimeException("Account is not active.");
        }

        return recruiter;
    }

    public Recruiter verifyRecruiter(String token) {
        Recruiter recruiter = recruiterRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        recruiter.setVerified(true);
        recruiter.setVerificationToken(null);
        return recruiterRepository.save(recruiter);
    }

    public Recruiter updateProfile(Long id, Recruiter updatedData) {
        Recruiter recruiter = recruiterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        if (updatedData.getCompanyName() != null) recruiter.setCompanyName(updatedData.getCompanyName());
        if (updatedData.getSector() != null) recruiter.setSector(updatedData.getSector());
        if (updatedData.getPresentation() != null) recruiter.setPresentation(updatedData.getPresentation());
        if (updatedData.getContact() != null) recruiter.setContact(updatedData.getContact());
        if (updatedData.getLocation() != null) recruiter.setLocation(updatedData.getLocation());
        if (updatedData.getLogo() != null) {
            recruiter.setLogo(updatedData.getLogo());
        }

        return recruiterRepository.save(recruiter);
    }

    public Optional<Recruiter> findById(Long id) {
        return recruiterRepository.findById(id);
    }
}
