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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


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

    List<LocalDate[]> eventDates = Arrays.asList(
            new LocalDate[]{LocalDate.of(2023, 9, 27), LocalDate.of(2023, 9, 27)},
            new LocalDate[]{LocalDate.of(2023, 10, 23), LocalDate.of(2023, 11, 3)}, // Juillet
            new LocalDate[]{LocalDate.of(2023, 12, 25), LocalDate.of(2024, 1, 5)},
            new LocalDate[]{LocalDate.of(2024, 2, 13), LocalDate.of(2024, 2, 13)},
            new LocalDate[]{LocalDate.of(2024, 2, 26), LocalDate.of(2024, 3, 8)},
            new LocalDate[]{LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 1)},
            new LocalDate[]{LocalDate.of(2024, 5, 20), LocalDate.of(2024, 5, 20)},
            new LocalDate[]{LocalDate.of(2024, 4, 29), LocalDate.of(2024, 5, 10)},
            new LocalDate[]{LocalDate.of(2024, 7, 6), LocalDate.of(2024, 8, 25)}
    );

    List<LocalDate> blockedDates = Arrays.asList(
            LocalDate.of(2023, 9, 27),
            LocalDate.of(2023, 2, 13),
            LocalDate.of(2023, 4, 1),
            LocalDate.of(2023,5,20)
    );

    LocalTime heureDebutMin = LocalTime.of(9, 0);
    LocalTime heureFinMax = LocalTime.of(21, 30);

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
    public ResponseEntity<?> addAbsence(@RequestBody Absence absence, UriComponentsBuilder builder, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_write:presence")) {
            Personne personne = personneRepository.findById(absence.getPersonne().getId()).orElse(null);
            List<Absence> absences = new ArrayList<>();
            LocalDate date = absence.getDate();
            String presence = absence.getPresence();
            LocalTime heuredebut = absence.getHeuredebut();
            LocalTime heurefin = absence.getHeurefin();

            boolean isDateInEvents = eventDates.stream().anyMatch(dates ->
                    (date.isEqual(dates[0]) || date.isAfter(dates[0])) &&
                            (date.isEqual(dates[1]) || date.isBefore(dates[1]))
            );

            if(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY){

                logger.debug("Échec de l'enregistrement de l'absence : la date ne peut pas être un week-end");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("erreur", "Une absence ne peut pas être encodé le week-end."));

            }

            if (isDateInEvents) {
                logger.debug("Échec de la validation de la date (pendant les événements)");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("erreur", "Une absence ne peut pas être encodé pendant les vacances ou jour fériée."));

            }

            if (!(presence.equalsIgnoreCase("A") || presence.equalsIgnoreCase("P"))) {
                logger.debug("Échec de la validation de la présence");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("erreur", "veuillez entre a ou A, p ou P."));

            }


            if (heuredebut.isBefore(heureDebutMin) || heurefin.isAfter(heureFinMax) || heuredebut.isAfter(heurefin)) {
                logger.debug("Échec de la validation des heures");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("erreur", "heure de début doit être min 09:00 ou avant heurefin, heuremax 21:30."));

            }

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
    public ResponseEntity<?> addAbsenceprof(@RequestBody Absence absence, UriComponentsBuilder builder, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_write:profabsent")) {
            Personne personne = personneRepository.findById(absence.getPersonne().getId()).orElse(null);
            List<Absence> absences = new ArrayList<>();
             String professor = personne.getNom() + " " + personne.getPrenom();
            LocalDate date = absence.getDate();
            LocalTime heuredebut = absence.getHeuredebut();
            LocalTime heurefin = absence.getHeurefin();
            String email = personne.getMail();
            String presence = absence.getPresence();

            boolean isDateInEvents = eventDates.stream().anyMatch(dates ->
                    (date.isEqual(dates[0]) || date.isAfter(dates[0])) &&
                            (date.isEqual(dates[1]) || date.isBefore(dates[1]))
            );

            if(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY){

                logger.debug("Échec de l'enregistrement de l'absence : la date ne peut pas être un week-end");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("erreur", "interdit de mettre absence sur un week-end"));
            }

            if (isDateInEvents) {
                logger.debug("Échec de la validation de la date (pendant les événements)");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("erreur", "absence ne peut être en vacance"));
            }

            if (!(presence.equalsIgnoreCase("A") || presence.equalsIgnoreCase("P"))) {
                logger.debug("Échec de la validation de la présence");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("erreur", "La présence doit être p ou P, a ou A"));
            }


            if (heuredebut.isBefore(heureDebutMin) || heurefin.isAfter(heureFinMax) || heuredebut.isAfter(heurefin)) {
                logger.debug("Échec de la validation des heures");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("erreur", "heuredebut min 09:00 ou heuremax 21:30, heure debut ne doit pas dépasser heuremax"));
            }

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
    public ResponseEntity<?> updateAbsence(@PathVariable UUID id, @RequestBody Absence updateAbsence, Authentication authentication) {
        if (!hasAuthority(authentication, "SCOPE_write:profabsent")) {
            logger.debug("Attention, vous n'avez pas la bonne permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
        Optional<Absence> existingAbsenceOptional = absenceRepository.findById(id);
        LocalDate date = updateAbsence.getDate();
        LocalTime heuredebut = updateAbsence.getHeuredebut();
        LocalTime heurefin = updateAbsence.getHeurefin();
        String presence = updateAbsence.getPresence();

        boolean isStartDateInEvents = eventDates.stream().anyMatch(dates ->
                (updateAbsence.getDate().isEqual(dates[0]) || updateAbsence.getDate().isAfter(dates[0])) &&
                        updateAbsence.getDate().isBefore(dates[1])
        );



        if (!existingAbsenceOptional.isPresent()) {
            logger.debug("L'ID de l'absence est inexistant");
            return ResponseEntity.notFound().build();
        }

        if (blockedDates.contains(updateAbsence.getDate())) {
            logger.debug("La date de l'absence tombe sur une date bloquée.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("erreur", "la date absence ne peut pas être sur un jour férié."));


        }
          if(isStartDateInEvents){
              logger.debug("L'absence tombe sur des vacances");
              return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                      .body(Collections.singletonMap("erreur", "absence tombe sur des vacances"));
          }


        if(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY){

            logger.debug("Échec de l'enregistrement de l'absence : la date ne peut pas être un week-end");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("erreur", "interdit de mettre absence sur un week-end"));

        }



        if (heuredebut.isBefore(heureDebutMin) || heurefin.isAfter(heureFinMax) || heuredebut.isAfter(heurefin)) {
            logger.debug("Échec de la validation des heures");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("erreur", "heure de début doit être min 09:00 ou avant heurefin, heuremax 21:30."));

        }

        if (!(presence.equalsIgnoreCase("A") || presence.equalsIgnoreCase("P"))) {
            logger.debug("Échec de la validation de la présence");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("erreur", "la présence doit être a ou A, p ou P"));

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
