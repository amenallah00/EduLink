package com.example.gestionAlumni.Controllers;

import com.example.gestionAlumni.Entities.Recruiter;
import com.example.gestionAlumni.Services.LoginRequest;
import com.example.gestionAlumni.Services.RecruiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiters")
@RequiredArgsConstructor
public class RecruiterController {

    private final RecruiterService recruiterService;

    @GetMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam String token) {
        try {
            recruiterService.verifyRecruiter(token);
            return ResponseEntity.ok("Account verified successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Recruiter recruiter) {
        try {
            return ResponseEntity.ok(recruiterService.signup(recruiter));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Recruiter recruiter = recruiterService.authenticate(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );
            return ResponseEntity.ok(recruiter);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<Recruiter> updateProfile(@PathVariable Long id, @RequestBody Recruiter updatedData) {
        return ResponseEntity.ok(recruiterService.updateProfile(id, updatedData));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recruiter> getProfile(@PathVariable Long id) {
        return recruiterService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
