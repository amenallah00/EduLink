package com.example.gestionAlumni.Controllers;


import com.example.gestionAlumni.DTO.StudentUpdateDTO;
import com.example.gestionAlumni.Entities.Student;
import com.example.gestionAlumni.Services.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.gestionAlumni.Services.LoginRequest;

import java.io.IOException;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam String token) {
        try {
            studentService.verifyStudent(token);
            return ResponseEntity.ok("Account verified successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Student student = studentService.authenticate(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );
            return ResponseEntity.ok(student);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Student student) {
        try {
            return ResponseEntity.ok(studentService.signup(student));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/complete-profile")
    public ResponseEntity<Student> completeProfile(@PathVariable Long id,
                                                 @RequestParam String speciality,
                                                 @RequestParam String searchType,
                                                 @RequestParam int predictedGradYear,
                                                 @RequestParam("document") MultipartFile document) {
        Student updatedStudent = studentService.completeProfile(id, speciality, searchType, predictedGradYear, document);
        return ResponseEntity.ok(updatedStudent);
    }
    @PutMapping("/{id}/profile")
    public ResponseEntity<Student> updateProfile(@PathVariable Long id, @RequestBody Student updatedData) {
        return ResponseEntity.ok(studentService.updateProfile(id, updatedData));
    }

    @PostMapping("/{id}/upload-resume")
    public ResponseEntity<Student> uploadResume(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(studentService.uploadResume(id, file));
    }
}
