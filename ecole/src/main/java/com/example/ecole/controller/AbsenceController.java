package com.example.ecole.controller;

import com.example.ecole.models.Absence;
import com.example.ecole.models.Personne;
import com.example.ecole.repository.AbsenceRepository;
import com.example.ecole.repository.PersonneRepository;
import com.example.ecole.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/add/absence")
public class AbsenceController {

    private static final Logger logger = LoggerFactory.getLogger(AbsenceController.class);


    @Autowired
    private AbsenceRepository absenceRepository;

    @Autowired
    private PersonneRepository personneRepository;

    @Autowired
    EmailService emailService;


    @Autowired
    public AbsenceController(AbsenceRepository absenceRepository, PersonneRepository personneRepository) {
        this.absenceRepository = absenceRepository;
        this.personneRepository = personneRepository;

    }

    @GetMapping
    public ResponseEntity<List<Absence>> getAllAbsence(Pageable pageable, Authentication authentication) {

        if (hasAuthority(authentication, "SCOPE_read:presence")) {
            List<Absence> absences = absenceRepository.findAll();
            return ResponseEntity.ok(absences);
        } else {

            logger.debug("il y a eu un problème de récupération");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    @GetMapping("/pagipresence")
    public ResponseEntity<Page<Absence>> getPaginatedAbsence(Pageable pageable, Authentication authentication) {
        Page<Absence> absences = absenceRepository.findAll(pageable);
        return ResponseEntity.ok(absences);
    }

    @PostMapping
    public ResponseEntity<Absence> addAbsence(@RequestBody Absence absence, UriComponentsBuilder builder, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_write:presence")) {
            Personne personne = personneRepository.findById(absence.getPersonne().getId()).orElse(null);
            List<Absence> absences = new ArrayList<>();

            if (personne != null) {
                absence.setPersonne(personne);
                absenceRepository.save(absence);
                absences.add(absence);
                personne.setAbsences(absences);
                URI location = builder.path("/add/absence/{id}").buildAndExpand(absence.getId()).toUri();
                logger.debug("Succès de l'ajout des données ");

                return ResponseEntity.created(location).body(absence);


            } else {
                logger.debug("Échec mauvaise permission");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

        } else {
            logger.debug("Échec mauvaise permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    @PostMapping("/prof")
    public ResponseEntity<Absence> addAbsenceprof(@RequestBody Absence absence, UriComponentsBuilder builder, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_write:profabsent")) {
            Personne personne = personneRepository.findById(absence.getPersonne().getId()).orElse(null);
            List<Absence> absences = new ArrayList<>();
             String professor = personne.getNom() + " " + personne.getPrenom();
            LocalDate date = absence.getDate();
            LocalTime heuredebut = absence.getHeuredebut();
            LocalTime heurefin = absence.getHeurefin();
            String email = personne.getMail();
            if (personne != null) {
                absence.setPersonne(personne);
                absenceRepository.save(absence);
                absences.add(absence);
                personne.setAbsences(absences);
                URI location = builder.path("/add/absence/{id}").buildAndExpand(absence.getId()).toUri();
                emailService.sendProfessorAbsenceNotificationEmail(email,professor, date, heuredebut,heurefin);
                logger.debug("Succès de l'ajout des données ");

                return ResponseEntity.created(location).body(absence);


            } else {
                logger.debug("Échec mauvaise permission");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

        } else {
            logger.debug("Échec mauvaise permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    @GetMapping("/personne")
    public ResponseEntity<List<Personne>> getAllPerso(Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_write:personne")) {
            List<Personne> personnes = personneRepository.findAll();
            logger.debug("Succès de l'affichage de la liste des profs");
            return ResponseEntity.ok(personnes);

        } else {
            logger.debug("Échec, mauvaise permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/etudiants/api1")
    public ResponseEntity<List<Personne>> getAllProfesseurs(Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_write:presence")) {
            List<Personne> etudiants = personneRepository.findAllByStatut("etudiant");
            logger.debug("Succès de l'affichage de la liste des profs");
            return ResponseEntity.ok(etudiants);
        } else {
            logger.debug("Échec, mauvaise permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/professeurs/api2")
    public ResponseEntity<List<Personne>> getAllProfesseurss(Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_write:personne")) {
            List<Personne> professeurs = personneRepository.findAllByStatut("professeur");
            logger.debug("Succès de l'affichage de la liste des profs");
            return ResponseEntity.ok(professeurs);
        } else {
            logger.debug("Échec, mauvaise permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/absences/{id}")
    public ResponseEntity<Absence> updateAbsence(@PathVariable UUID id, @RequestBody Absence updateAbsence, Authentication authentication) {
        if (!hasAuthority(authentication, "SCOPE_write:profabsent")) {
            logger.debug("Attention, vous n'avez pas la bonne permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
        Optional<Absence> existingAbsenceOptional = absenceRepository.findById(id);

        if (!existingAbsenceOptional.isPresent()) {
            logger.debug("L'ID de l'absence est inexistant");
            return ResponseEntity.notFound().build();
        }

        Absence existingAbsence = existingAbsenceOptional.get();

        if (updateAbsence.getPresence() != null) {
            existingAbsence.setPresence(updateAbsence.getPresence());
        }

        if (updateAbsence.getDate() != null) {
            existingAbsence.setDate(updateAbsence.getDate());
        }

        if (updateAbsence.getHeuredebut() != null) {
            existingAbsence.setHeuredebut(updateAbsence.getHeuredebut());
        }

        if (updateAbsence.getHeurefin() != null) {
            existingAbsence.setHeurefin(updateAbsence.getHeurefin());
        }

        if (updateAbsence.isCertficat()) {
            existingAbsence.setCertficat(updateAbsence.isCertficat());
        }

        absenceRepository.save(existingAbsence);

        logger.debug("Réussite de la mise à jour des champs pour l'absence avec l'ID : " + id);
        return ResponseEntity.ok(existingAbsence);
    }




    private static boolean hasAuthority(Authentication authentication, String expectedAuthority) {
        logger.debug("autorisation");
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(expectedAuthority));
    }
}
