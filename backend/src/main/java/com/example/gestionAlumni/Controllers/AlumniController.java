package com.example.gestionAlumni.Controllers;

import com.example.gestionAlumni.DTO.AlumniPreferencesDTO;
import com.example.gestionAlumni.Entities.Alumni;
import com.example.gestionAlumni.Services.AlumniService;
import com.example.gestionAlumni.Services.LoginRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/alumni")

public class AlumniController {
    private final AlumniService alumniService;

    @Autowired
    public AlumniController(AlumniService alumniService) {
        this.alumniService = alumniService;
    }

    @GetMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam String token) {
        try {
            alumniService.verifyAlumni(token);
            return ResponseEntity.ok("Account verified successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Alumni alumni) {
        try {
            Alumni savedAlumni = alumniService.signup(alumni);
            return ResponseEntity.ok(savedAlumni);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Alumni alumni = alumniService.authenticate(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );
            return ResponseEntity.ok(alumni);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}/job")
    public ResponseEntity<Alumni> updateJobTitle(@PathVariable Long id, @RequestParam String newJobTitle) {
        return ResponseEntity.ok(alumniService.updateJobTitle(id, newJobTitle));
    }

    @PutMapping("/{id}/company")
    public ResponseEntity<Alumni> updateCompany(@PathVariable Long id, @RequestParam String newCompany) {
        return ResponseEntity.ok(alumniService.updateCompany(id, newCompany));
    }

    @PutMapping("/{id}/preferences")
    public ResponseEntity<Alumni> updatePreferences(@PathVariable Long id,
                                                     @RequestBody AlumniPreferencesDTO preferences) {
        Alumni updatedAlumni = alumniService.updatePreferences(id, preferences);
        return new ResponseEntity<>(updatedAlumni, HttpStatus.OK);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<Alumni> updateProfile(@PathVariable Long id, @RequestBody Alumni updatedData) {
        return ResponseEntity.ok(alumniService.updateProfile(id, updatedData));
    }

    @PostMapping("/{id}/upload-resume")
    public ResponseEntity<Alumni> uploadResume(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(alumniService.uploadResume(id, file));
    }

    @GetMapping("/directory")
    public ResponseEntity<List<Alumni>> getAllVerifiedAlumni() {
        return ResponseEntity.ok(alumniService.findAllVerified());
    }

    @GetMapping("/directory/search")
    public ResponseEntity<List<Alumni>> searchAlumni(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String sector,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) Integer graduationYear,
            @RequestParam(required = false) String skill) {
        return ResponseEntity.ok(alumniService.searchAlumni(query, sector, company, graduationYear, skill));
    }

    @GetMapping("/directory/{id}")
    public ResponseEntity<Alumni> getAlumniById(@PathVariable Long id) {
        return ResponseEntity.ok(alumniService.findById(id));
    }

    @GetMapping("/mentors/search")
    public ResponseEntity<List<Alumni>> searchMentors(
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String job,
            @RequestParam(required = false) String sector,
            @RequestParam(required = false) String speciality,
            @RequestParam(required = false) String skill) {
        return ResponseEntity.ok(alumniService.searchMentors(company, job, sector, speciality, skill));
    }

    @GetMapping("/mentors/top-rated")
    public ResponseEntity<List<Alumni>> getTopRatedMentors(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(alumniService.getTopRatedMentors(limit));
    }

    @GetMapping("/{id}/mentor-stats")
    public ResponseEntity<?> getMentorStats(@PathVariable Long id) {
        return ResponseEntity.ok(alumniService.getMentorStats(id));
    }

    @GetMapping("/{id}/rating")
    public ResponseEntity<Double> getMentorAverageRating(@PathVariable Long id) {
        return ResponseEntity.ok(alumniService.getAverageRating(id));
    }
}
