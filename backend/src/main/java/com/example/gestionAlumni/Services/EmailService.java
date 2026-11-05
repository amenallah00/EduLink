package com.example.gestionAlumni.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    public void sendVerificationEmail(String toEmail, String firstName, String token, String userType) {
        String verificationUrl = "http://localhost:4200/verify-email?token=" + token + "&type=" + userType;
        String subject = "Vérifiez votre compte EduLink";
        String body = "Bonjour " + firstName + ",\n\n"
                + "Merci d'avoir créé un compte sur EduLink !\n"
                + "Veuillez cliquer sur le lien ci-dessous pour vérifier votre adresse email et activer votre compte :\n\n"
                + verificationUrl + "\n\n"
                + "À très bientôt sur EduLink !";
        
        sendEmail(toEmail, subject, body);
    }
}