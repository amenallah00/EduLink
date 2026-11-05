package com.example.gestionAlumni.Repos;

import com.example.gestionAlumni.Entities.CompanyReview;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CompanyReviewRepository extends JpaRepository<CompanyReview, Long> {
    List<CompanyReview> findByCompanyName(String companyName);
}
