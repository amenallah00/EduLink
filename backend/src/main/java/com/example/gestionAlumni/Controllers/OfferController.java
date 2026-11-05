package com.example.gestionAlumni.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.gestionAlumni.DTO.OffreDTO;
import com.example.gestionAlumni.Entities.Offer;
import com.example.gestionAlumni.Services.OfferService;

@RestController
@RequestMapping("/api/offers")
public class OfferController {

    @Autowired
    private OfferService offerService;

    // Tous les offres, triées par date
    @GetMapping
    public List<Offer> getAllOffers() {
        return offerService.getAllOffersSortedByDate();
    }

    // Recherche par titre
    @GetMapping("/search")
    public List<Offer> searchOffers(@RequestParam String title) {
        return offerService.searchOffersByTitle(title);
    }

    // Récupérer une offre spécifique par ID
    @GetMapping("/{id}")
    public ResponseEntity<Offer> getOfferById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(offerService.getOfferById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{id}/offers")
    public ResponseEntity<Offer> createOffer(
            @PathVariable Long id,
            @RequestBody OffreDTO  offerDTO) {
        return ResponseEntity.ok(offerService.createOffer(id, offerDTO));
    }
}