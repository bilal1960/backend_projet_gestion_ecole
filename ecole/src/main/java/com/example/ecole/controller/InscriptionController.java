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
import java.util.*;

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

    List<LocalDate[]> eventinscrit = Arrays.asList(
            new LocalDate[]{LocalDate.of(2023, 7, 3), LocalDate.of(2023, 7, 7)},
            new LocalDate[]{LocalDate.of(2023, 7, 10), LocalDate.of(2023, 7, 14)},
            new LocalDate[]{LocalDate.of(2023, 8, 14), LocalDate.of(2023, 8, 18)},
            new LocalDate[]{LocalDate.of(2023, 8, 21), LocalDate.of(2023, 8, 25)}

    );

    private boolean isDateValid(LocalDate date) {
        return eventinscrit.stream().anyMatch(dates ->
                !date.isBefore(dates[0]) && !date.isAfter(dates[1])
        );
    }



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
    public ResponseEntity<?> addInscrit(@RequestBody Inscription inscription, UriComponentsBuilder builder, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_write:inscrit")) {
            Personne personne = personneRepository.findById(inscription.getPersonne().getId()).orElse(null);
            List<Inscription> inscriptions = new ArrayList<>();
            LocalDate deadline = LocalDate.of(2023,9,7);

            if (!isDateValid(inscription.getDate_inscrit())) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("erreur", "La date d'inscription doit être 03/07/2023 au 07/07/2023, 10/07/2023 au 14/07/2023 ou 14/08/2023 au 18/08/2023, 21/08/2023 au 25/08/2023."));
            }

            if(inscription.getMinerval() >300){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("erreur", "Le minerval doit être inférieur ou égal à 300"));

            }

            if (personne != null) {
                inscription.setPersonne(personne);
                inscritRepository.save(inscription);
                inscriptions.add(inscription);
                personne.setInscriptions(inscriptions);
                URI location = builder.path("/add/inscriptions/{id}").buildAndExpand(inscription.getId()).toUri();
                emailService.sendRegistrationConfirmationEmail(personne.getMail(),deadline,inscription.getSection(),inscription.getSecondaire_anne());
                return ResponseEntity.created(location).body(inscription);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/inscrit/{id}")
    public ResponseEntity<?> updateInscription(@PathVariable UUID id, @Valid @RequestBody Inscription updatedInscription, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_write:inscrit")) {
            Optional<Inscription> optionalInscription = inscritRepository.findById(id);

            if (optionalInscription.isPresent()) {
                Inscription inscription = optionalInscription.get();

                if(updatedInscription.getMinerval() < updatedInscription.getRembourser() || updatedInscription.getRembourser() <0){

                    logger.debug("Le remboursement doit être inférieur au minerval");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Collections.singletonMap("erreur", "Le remboursement doit être inférieur au minerval"));
                }

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

                if(updatedInscription.getMinerval() >300){
                    logger.debug("Le minerval doit être <=300 ou >=0");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Collections.singletonMap("erreur", "Le minerval ne peut pas être supérieur à 300"));
                }

                if(updatedInscription.getMinerval() <0){
                    logger.debug("Le mineval doit être >=0");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Collections.singletonMap("erreur", "Le minerval ne peut pas être <0"));

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


