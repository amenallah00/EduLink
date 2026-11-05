package com.example.gestionAlumni.Repos;

import com.example.gestionAlumni.Entities.KnowledgeBase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KnowledgeBaseRepository extends JpaRepository<KnowledgeBase, Long> {
    List<KnowledgeBase> findByCategory(String category);
    List<KnowledgeBase> findByCompanyName(String companyName);
}
