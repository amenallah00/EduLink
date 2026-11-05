package com.example.gestionAlumni;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Lightweight seeder that ensures an admin account always exists.
 * Uses only native SQL to avoid Hibernate mapping issues with null boolean columns.
 */
@Component
public class AdminSeeder implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    public AdminSeeder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        try {
            // 1. Fix legacy DTYPE values
            entityManager.createNativeQuery(
                "UPDATE user SET dtype='admin' WHERE dtype='Administrator'"
            ).executeUpdate();

            // 2. Fix NULL boolean columns that crash Hibernate mapping
            entityManager.createNativeQuery(
                "UPDATE user SET verified = false WHERE verified IS NULL"
            ).executeUpdate();
            entityManager.createNativeQuery(
                "UPDATE user SET active = true WHERE active IS NULL"
            ).executeUpdate();

            // 3. Check if admin exists via native query (avoids Hibernate mapping)
            Number count = (Number) entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM user WHERE email = 'test@admin.com' AND dtype = 'admin'"
            ).getSingleResult();

            String encodedPassword = passwordEncoder.encode("password123");

            if (count.intValue() > 0) {
                // Update existing admin
                entityManager.createNativeQuery(
                    "UPDATE user SET password = :pwd, active = true, verified = true WHERE email = 'test@admin.com' AND dtype = 'admin'"
                )
                .setParameter("pwd", encodedPassword)
                .executeUpdate();
                System.out.println("ADMIN PASSWORD RESET (test@admin.com / password123)");
            } else {
                // Create new admin
                entityManager.createNativeQuery(
                    "INSERT INTO user (dtype, first_name, last_name, email, password, active, verified, graduation_year) " +
                    "VALUES ('admin', 'Super', 'Admin', 'test@admin.com', :pwd, true, true, 0)"
                )
                .setParameter("pwd", encodedPassword)
                .executeUpdate();
                System.out.println("DEFAULT ADMIN CREATED (test@admin.com / password123)");
            }
        } catch (Exception e) {
            System.err.println("⚠️ AdminSeeder error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
