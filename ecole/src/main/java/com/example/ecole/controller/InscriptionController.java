package com.example.ecole.controller;

import com.example.ecole.models.Inscription;
import com.example.ecole.models.Personne;
import com.example.ecole.repository.InscriptionRepository;
import com.example.ecole.repository.PersonneRepository;
import com.example.ecole.service.EmailService;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;

@RestController
@RequestMapping("/add/inscriptions")
public class InscriptionController {
    private static final Logger logger = LoggerFactory.getLogger(InscriptionController.class);
    @Autowired
    private InscriptionRepository inscritRepository;
    @Autowired
    private PersonneRepository personneRepository;

    @Autowired
    private EmailService emailService;


    @Autowired
    public InscriptionController(InscriptionRepository inscriptionRepository, PersonneRepository personneRepository) {
        this.inscritRepository = inscriptionRepository;
        this.personneRepository = personneRepository;
    }

    @GetMapping
    public ResponseEntity<List<Inscription>> getAllInscription(Pageable pageable, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_read:inscrit")) {
            List<Inscription> inscriptions = inscritRepository.findAll();
            logger.debug("la récupération est un succès");
            return ResponseEntity.ok(inscriptions);
        } else {
            logger.debug("il y a eu un problème de récupération");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/api")
    public ResponseEntity<Page<Inscription>> getPaginatedInscriptions(Pageable pageable, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_read:inscrit")) {
            Page<Inscription> inscriptions = inscritRepository.findAll(pageable);
            logger.debug("La récupération paginée est un succès");
            return ResponseEntity.ok(inscriptions);
        } else {
            logger.debug("Il y a eu un problème de récupération");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping
    public ResponseEntity<Inscription> addInscrit(@RequestBody Inscription inscription, UriComponentsBuilder builder, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_write:inscrit")) {
            Personne personne = personneRepository.findById(inscription.getPersonne().getId()).orElse(null);
            List<Inscription> inscriptions = new ArrayList<>();
            LocalDate deadline = LocalDate.of(2023,9,7);

            if (personne != null) {
                inscription.setPersonne(personne);
                inscritRepository.save(inscription);
                inscriptions.add(inscription);
                personne.setInscriptions(inscriptions);
                URI location = builder.path("/add/inscriptions/{id}").buildAndExpand(inscription.getId()).toUri();
                logger.debug("Succès de l'ajout des données ");
                emailService.sendRegistrationConfirmationEmail(personne.getMail(), deadline);
                return ResponseEntity.created(location).body(inscription);
            } else {
                logger.debug("La personne ou la matiere correspondante n'a pas été trouvée");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

        } else {
            logger.debug("Échec mauvaise permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/inscrit/{id}")
    public ResponseEntity<Inscription> updateInscription(@PathVariable UUID id, @Valid @RequestBody Inscription updatedInscription, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_write:inscrit")) {
            Optional<Inscription> optionalInscription = inscritRepository.findById(id);

            if (optionalInscription.isPresent()) {
                Inscription inscription = optionalInscription.get();

                if (updatedInscription.getRembourser() != null) {
                    inscription.setRembourser(updatedInscription.getRembourser());
                }
                if (updatedInscription.getSection() != null) {
                    inscription.setSection(updatedInscription.getSection());
                }
                if (updatedInscription.getSecondaire_anne() != null) {
                    inscription.setSecondaire_anne(updatedInscription.getSecondaire_anne());
                }
                if (updatedInscription.getCommune() != null) {
                    inscription.setCommune(updatedInscription.getCommune());
                }
                if (updatedInscription.getMinerval() != null) {
                    inscription.setMinerval(updatedInscription.getMinerval());
                }

                inscription = inscritRepository.save(inscription);

                logger.debug("Succès de la mise à jour des données");
                return ResponseEntity.ok(inscription);
            } else {
                logger.debug("L'inscription correspondante n'a pas été trouvée");
                return ResponseEntity.notFound().build();
            }
        } else {
            logger.debug("Échec mauvaise permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }



    private static boolean hasAuthority(Authentication authentication, String expectedAuthority) {
        logger.debug("autorisation");
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(expectedAuthority));
    }
}


