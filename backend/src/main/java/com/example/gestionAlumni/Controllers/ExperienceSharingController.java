package com.example.gestionAlumni.Controllers;

import com.example.gestionAlumni.Entities.CompanyReview;
import com.example.gestionAlumni.Entities.KnowledgeBase;
import com.example.gestionAlumni.Repos.CompanyReviewRepository;
import com.example.gestionAlumni.Repos.KnowledgeBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experience")
public class ExperienceSharingController {

    @Autowired
    private CompanyReviewRepository companyReviewRepository;

    @Autowired
    private KnowledgeBaseRepository knowledgeBaseRepository;

    @PostMapping("/reviews")
    public CompanyReview postReview(@RequestBody CompanyReview review) {
        return companyReviewRepository.save(review);
    }

    @GetMapping("/reviews/company/{name}")
    public List<CompanyReview> getCompanyReviews(@PathVariable String name) {
        return companyReviewRepository.findByCompanyName(name);
    }

    @GetMapping("/reviews/all")
    public List<CompanyReview> getAllCompanyReviews() {
        return companyReviewRepository.findAll();
    }

    @PostMapping("/knowledge")
    public KnowledgeBase postKnowledge(@RequestBody KnowledgeBase kb) {
        return knowledgeBaseRepository.save(kb);
    }

    @GetMapping("/knowledge/category/{category}")
    public List<KnowledgeBase> getKnowledgeByCategory(@PathVariable String category) {
        return knowledgeBaseRepository.findByCategory(category);
    }

    @GetMapping("/knowledge/company/{name}")
    public List<KnowledgeBase> getKnowledgeByCompany(@PathVariable String name) {
        return knowledgeBaseRepository.findByCompanyName(name);
    }

    @GetMapping("/knowledge/all")
    public List<KnowledgeBase> getAllKnowledgeBase() {
        return knowledgeBaseRepository.findAll();
    }
}
