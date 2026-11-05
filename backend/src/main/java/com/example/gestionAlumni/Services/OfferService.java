package com.example.gestionAlumni.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gestionAlumni.DTO.OffreDTO;
import com.example.gestionAlumni.Entities.Alumni;
import com.example.gestionAlumni.Entities.Offer;
import com.example.gestionAlumni.Repos.AlumniRepository;
import com.example.gestionAlumni.Repos.OfferRepository;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private  AlumniRepository alumniRepository;

    public List<Offer> getAllOffersSortedByDate() {
        return offerRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Offer> searchOffersByTitle(String title) {
        return offerRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(title);
    }

    public Offer getOfferById(Long id) {
        return offerRepository.findById(id).orElseThrow(() -> new RuntimeException("Offer not found"));
    }
 

    public Offer createOffer(Long alumniId, OffreDTO dto) {
        Alumni alumni = alumniRepository.findById(alumniId)
                .orElseThrow(() -> new RuntimeException("Alumni not found"));

        Offer offer = new Offer();
        offer.setType(dto.getType());
        offer.setDuration(dto.getDuration());
        offer.setCompany(dto.getCompany());
        offer.setPosition(dto.getPosition());
        offer.setProposedSalary(dto.getProposedSalary());
        offer.setTitle(dto.getTitle());
        offer.setDescription(dto.getDescription());
        offer.setCreator(alumni);

        return offerRepository.save(offer);
    }
}