package com.example.ecole.controller;

import com.example.ecole.models.Personne;
import com.example.ecole.models.Vacance;
import com.example.ecole.repository.PersonneRepository;
import com.example.ecole.repository.VacanceRepository;
import com.example.ecole.service.EmailService;
import org.slf4j.Logger;
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

@RestController
@RequestMapping("/add/vacance")
public class VacanceController {

    @Autowired
    private VacanceRepository vacanceRepository;

    @Autowired
    private PersonneRepository personneRepository;
    @Autowired
    private EmailService emailService;


    private static final Logger logger = LoggerFactory.getLogger(VacanceController.class);

    public VacanceController(VacanceRepository vacanceRepository, PersonneRepository personneRepository) {

        this.vacanceRepository = vacanceRepository;
        this.personneRepository = personneRepository;
    }

    @GetMapping
    public ResponseEntity<List<Vacance>> getAllVacance(Pageable pageable,Authentication authentication) {

        if (hasAuthority(authentication, "SCOPE_read:vacance")) {
            List<Vacance> vacances = vacanceRepository.findAll();
            return ResponseEntity.ok(vacances);
        } else {
            logger.debug("Failed due to insufficient permissions");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/pagivac")
    public ResponseEntity<Page<Vacance>> getAllVacancepagi(Pageable pageable,Authentication authentication) {

        if (hasAuthority(authentication, "SCOPE_read:vacance")) {
            Page<Vacance> pageVacances = vacanceRepository.findAll(pageable);
            return ResponseEntity.ok(pageVacances);
        } else {
            logger.debug("Failed due to insufficient permissions");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/personnet")
    public ResponseEntity<List<Personne>> getAlltable(Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_read:personne")) {
            List<Personne> personness = personneRepository.findAll();
            logger.debug("succès de l'affichage de la liste personne");

            return ResponseEntity.ok(personness);
        } else {
            logger.debug("échec mauvaise permission ");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

        @PostMapping
        public ResponseEntity<Vacance> addVacance(@RequestBody Vacance vacances, Authentication authentication, UriComponentsBuilder builder) {
            if (hasAuthority(authentication, "SCOPE_write:vacance")) {
                Personne personne = personneRepository.findById(vacances.getPersonne().getId()).orElse(null);
                List<Vacance> vacances1 = new ArrayList<>();
                LocalDate debut = vacances.getDateDebut();
                LocalDate fin = vacances.getDateFin();
                String commentaire = vacances.getCommentaire();
                String studentName = personne.getPrenom() + ""+ personne.getNom();
                String emailto = personne.getMail();


                if (personne != null) {

                    vacances.setPersonne(personne);
                    vacanceRepository.save(vacances);
                    vacances1.add(vacances);
                    personne.setVacances(vacances1);
                    personneRepository.save(personne);
                    URI location = builder.path("/add/vacance/{id}").buildAndExpand(vacances.getId()).toUri();

                    emailService.sendVacationNotificationEmail(emailto,studentName,commentaire,debut,fin);
                    logger.debug("succès de la sauvegarde des données dans la database");
                    return ResponseEntity.created(location).body(vacances);

                } else {
                    logger.debug("Échec de la création de la vacance : personne inexistante.");
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            }else {
                logger.debug("Échec mauvaise permission");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

        }

    @PutMapping("/vacances/{id}")
    public ResponseEntity<Vacance> updateVacance(@PathVariable UUID id, @RequestBody Vacance updateVacance, Authentication authentication) {
        if (!hasAuthority(authentication, "SCOPE_write:profabsent")) {
            logger.debug("Attention, vous n'avez pas la bonne permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
        Optional<Vacance> existingVacanceOptional = vacanceRepository.findById(id);

        if (!existingVacanceOptional.isPresent()) {
            logger.debug("L'ID de l'absence est inexistant");
            return ResponseEntity.notFound().build();
        }

        Vacance existingVacance = existingVacanceOptional.get();

        if (updateVacance.getDateDebut() != null) {
            existingVacance.setDatedebut(updateVacance.getDateDebut());
        }

        if (updateVacance.getDateFin() != null) {
            existingVacance.setDatefin(updateVacance.getDateFin());
        }

        if (updateVacance != null) {
            existingVacance.setType(updateVacance.getType());
        }

        if (updateVacance != null) {
            existingVacance.setCommentaire(updateVacance.getCommentaire());
        }

        vacanceRepository.save(existingVacance);

        logger.debug("Réussite de la mise à jour des champs pour l'absence avec l'ID : " + id);
        return ResponseEntity.ok(existingVacance);
    }



    private static boolean hasAuthority(Authentication authentication, String expectedAuthority) {
        logger.debug("vérifier l'autorité de permission", expectedAuthority);
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(expectedAuthority));
    }
}
