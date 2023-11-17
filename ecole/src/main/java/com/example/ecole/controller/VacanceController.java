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
import java.util.*;

@RestController
@RequestMapping("/add/vacance")
public class VacanceController {

    @Autowired
    private VacanceRepository vacanceRepository;

    @Autowired
    private PersonneRepository personneRepository;
    @Autowired
    private EmailService emailService;

    List<LocalDate[]> eventDates = Arrays.asList(
            new LocalDate[]{LocalDate.of(2023, 9, 27), LocalDate.of(2023, 9, 27)},
            new LocalDate[]{LocalDate.of(2023, 10, 23), LocalDate.of(2023, 11, 3)},
            new LocalDate[]{LocalDate.of(2023, 12, 25), LocalDate.of(2024, 1, 5)},
            new LocalDate[]{LocalDate.of(2024, 2, 13), LocalDate.of(2024, 2, 13)},
            new LocalDate[]{LocalDate.of(2024, 2, 26), LocalDate.of(2024, 3, 8)},
            new LocalDate[]{LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 1)},
            new LocalDate[]{LocalDate.of(2024, 5, 20), LocalDate.of(2024, 5, 20)},
            new LocalDate[]{LocalDate.of(2024, 4, 29), LocalDate.of(2024, 5, 10)},
            new LocalDate[]{LocalDate.of(2024, 7, 6), LocalDate.of(2024, 8, 25)}

    );

    private boolean areDatesValid(LocalDate start, LocalDate end) {
        return eventDates.stream().anyMatch(dates ->
                (start.isEqual(dates[0]) || start.isAfter(dates[0])) &&
                        (end.isEqual(dates[1]) || end.isBefore(dates[1]))
        );
    }

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
                String typevac = vacances.getType();

                if (!areDatesValid(vacances.getDateDebut(), vacances.getDateFin())) {
                    logger.debug("Les dates de vacance ne sont pas dans les périodes autorisées.");
                    return ResponseEntity.badRequest().body(null);
                }

                if(!typevac.equals("vacance scolaire")&& !typevac.equals("férié")){
                    logger.debug("donné incorrecte: veuillez entrer vacance scolaire ou férié");
                    return ResponseEntity.badRequest().body(null);
                }

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

        String typevac = updateVacance.getType();
        Vacance existingVacance = existingVacanceOptional.get();
        if (!existingVacanceOptional.isPresent()) {
            logger.debug("L'ID de vacance est inexistant");
            return ResponseEntity.notFound().build();
        }

        if(!typevac.equals("vacance scolaire")&& !typevac.equals("férié")){
            logger.debug("donné incorrecte: veuillez entrer vacance scolaire ou férié");
            return ResponseEntity.badRequest().body(null);
        }


        if (updateVacance.getDateDebut() != null && updateVacance.getDateFin() != null) {
            if (!areDatesValid(updateVacance.getDateDebut(), updateVacance.getDateFin())) {
                logger.debug("Les dates de vacance ne sont pas dans les périodes autorisées.");
                return ResponseEntity.badRequest().body(null);
            }

            existingVacance.setDatedebut(updateVacance.getDateDebut());
            existingVacance.setDatefin(updateVacance.getDateFin());
        } else {
            logger.debug("Les deux dates doivent être fournies ensemble pour la mise à jour.");
            return ResponseEntity.badRequest().body(null);
        }

        if (updateVacance.getType() != null) {
            existingVacance.setType(updateVacance.getType());
        }

        if (updateVacance.getCommentaire() != null) {
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
