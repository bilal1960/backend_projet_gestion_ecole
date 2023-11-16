package com.example.ecole.controller;

import com.example.ecole.models.Personne;
import com.example.ecole.repository.MatiereRepository;
import com.example.ecole.repository.PersonneRepository;
import com.example.ecole.service.EmailService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.util.*;
import java.util.regex.Pattern;
import com.example.ecole.models.Matiere;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/add/matiere")
public class MatiereController {
    private static final Logger logger = LoggerFactory.getLogger(MatiereController.class);
    @Autowired
    private MatiereRepository matiererepository;
    @Autowired
    private PersonneRepository personneRepository;

    @Autowired
    private EmailService emailService;
    int currentYear = Year.now().getValue();
    LocalTime heureminimum = LocalTime.of(9, 0);
    String[] joursSemaineList = {"lundi", "mardi", "mercredi", "jeudi", "vendredi"};
    String localPattern = "^[a-zA-Z][a-zA-Z0-9]*$";
    List<LocalDate> blockedDates = Arrays.asList(
            LocalDate.of(2023, 9, 27),
            LocalDate.of(2023, 2, 13),
            LocalDate.of(2023, 4, 1),
            LocalDate.of(2023,5,20)
    );

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





    public MatiereController(MatiereRepository matiererepository, PersonneRepository personneRepository) {
        this.matiererepository = matiererepository;
        this.personneRepository = personneRepository;
    }

    @GetMapping("/matieres")
    public ResponseEntity<List<Matiere>> getAllMatieres(Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_read:matiere")) {
            List<Matiere> matieres = matiererepository.findAll();
            logger.debug("succès de l'affichage de la liste");
            return ResponseEntity.ok(matieres);
        } else {
            logger.debug("échec mauvaise permission ");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/matieress")
    public ResponseEntity<Matiere> addMatiere(@RequestBody Matiere matiere, UriComponentsBuilder builder, Authentication authentication) {
        int debutYear = matiere.getDebut().getYear();
        int finYear = matiere.getFin().getYear();
        List<Matiere> matieress = new ArrayList<>();

        boolean isStartDateInEvents = eventDates.stream().anyMatch(dates ->
                (matiere.getDebut().isEqual(dates[0]) || matiere.getDebut().isAfter(dates[0])) &&
                        matiere.getDebut().isBefore(dates[1])
        );

        boolean isEndDateInEvents = eventDates.stream().anyMatch(dates ->
                (matiere.getFin().isEqual(dates[1]) || matiere.getFin().isBefore(dates[1])) &&
                        matiere.getFin().isAfter(dates[0])
        );

        if (hasAuthority(authentication, "SCOPE_write:matiere")) {
            Personne professeur = personneRepository.findById(matiere.getPersonne().getId()).orElse(null);
            if (professeur != null) {
                if (matiere.getDebutime().isAfter(matiere.getFintime()) || matiere.getDebutime().isBefore(heureminimum)) {
                    logger.debug("vérifie que l'heure de début n'est pas après l'heure de fin ou avant 09:00");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
                if (matiere.getDebut().isAfter(matiere.getFin()) || debutYear != currentYear && debutYear != currentYear - 1 || finYear != currentYear && finYear != currentYear + 1) {
                    logger.debug("le format de la date est incorrecte ");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                if (finYear > debutYear + 1) {
                    logger.debug("l'année de fin ne peut pas être supérieur à 2 année de debut");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

                }

                if (blockedDates.contains(matiere.getDebut()) || blockedDates.contains(matiere.getFin())) {
                    logger.debug("La date du cours tombe sur une date bloquée.");
                    return ResponseEntity.badRequest().body(null);
                }


                if(isStartDateInEvents || isEndDateInEvents){
                    logger.debug("un cours ne peut être encodé pendant les vacances, jours fériés");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

                }

                if(matiere.getDebut().getDayOfWeek()== DayOfWeek.SATURDAY || matiere.getDebut().getDayOfWeek() == DayOfWeek.SUNDAY){
                    logger.debug("Les dates des matières ne peuvent se donner en week end");
                    return ResponseEntity.badRequest().body(null);
                }

                if(matiere.getFin().getDayOfWeek()== DayOfWeek.SATURDAY || matiere.getFin().getDayOfWeek() == DayOfWeek.SUNDAY){
                    logger.debug("Les dates des matières ne peuvent se donner en week end");
                    return ResponseEntity.badRequest().body(null);
                }

                matiere.setPersonne(professeur);
                matiererepository.save(matiere);
                matieress.add(matiere);
                professeur.setMatieres(matieress);
                personneRepository.save(professeur);
                URI location = builder.path("/add/matieres/{id}").buildAndExpand(matiere.getId()).toUri();

                String emailTo = professeur.getMail();
                LocalDate courseDate = matiere.getDebut();
                String courseName = matiere.getNom();
                String courseStartTime = matiere.getDebutime().toString();
                String courseEndTime = matiere.getFintime().toString();
                logger.debug("succès de la sauvegarde des données dans la base de données");
                emailService.sendCourseConfirmationEmail(emailTo, courseDate, courseName, courseStartTime, courseEndTime);
                return ResponseEntity.created(location).body(matiere);
            } else {
                logger.debug("Le professeur correspondant n'a pas été trouvé");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } else {
            logger.debug("Attention, vous n'avez pas la bonne permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/matieres/api")
    public ResponseEntity<Page<Matiere>> getPaginatedMatieres(Pageable pageable, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_read:matiere")) {
            Page<Matiere> matieres = matiererepository.findAll(pageable);
            logger.debug("La récupération paginée est un succès");
            return ResponseEntity.ok(matieres);
        } else {
            logger.debug("Il y a eu un problème de récupération");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/professeurs/api1")
    public ResponseEntity<List<Personne>> getAllProfesseurs(Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_read:matiere")) {
            List<Personne> professeurs = personneRepository.findAllByStatut("professeur");
            logger.debug("Succès de l'affichage de la liste des profs");
            return ResponseEntity.ok(professeurs);
        } else {
            logger.debug("Échec, mauvaise permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/matieres/{id}")
    public ResponseEntity<Matiere> updateMatiere(@PathVariable UUID id, @RequestBody Matiere updatedMatiere, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_write:matiere")) {
            Optional<Matiere> existingMatiereOptional = matiererepository.findById(id);

            boolean isStartDateInEvents = eventDates.stream().anyMatch(dates ->
                    (updatedMatiere.getDebut().isEqual(dates[0]) || updatedMatiere.getDebut().isAfter(dates[0])) &&
                            updatedMatiere.getDebut().isBefore(dates[1])
            );

            boolean isEndDateInEvents = eventDates.stream().anyMatch(dates ->
                    (updatedMatiere.getFin().isEqual(dates[1]) || updatedMatiere.getFin().isBefore(dates[1])) &&
                            updatedMatiere.getFin().isAfter(dates[0])
            );

            if (existingMatiereOptional.isPresent()) {
                Matiere existingMatiere = existingMatiereOptional.get();

                if (updatedMatiere.getDebut() != null && updatedMatiere.getFin() != null) {


                    if (blockedDates.contains(updatedMatiere.getDebut()) || blockedDates.contains(updatedMatiere.getFin())) {
                        logger.debug("La date du cours tombe sur une date bloquée.");
                        return ResponseEntity.badRequest().body(null);
                    }

                    if (isStartDateInEvents|| isEndDateInEvents) {
                        logger.debug("La mise à jour des dates pendant les périodes de vacances ou les jours fériés n'est pas autorisée");
                        return ResponseEntity.badRequest().body(null);
                    }

                    existingMatiere.setDebut(updatedMatiere.getDebut());
                    existingMatiere.setFin(updatedMatiere.getFin());
                }

                if (updatedMatiere.getDebut() != null && updatedMatiere.getFin() != null) {
                    int debutYear = updatedMatiere.getDebut().getYear();
                    int finYear = updatedMatiere.getFin().getYear();

                    if (updatedMatiere.getFin().isBefore(updatedMatiere.getDebut())) {
                        logger.debug("veuiller insérer une date fin supérieur à date début ou une année correcte");
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST).build();
                    }

                    if (finYear != currentYear && finYear != currentYear + 1 || debutYear != currentYear && debutYear != currentYear - 1 || finYear > debutYear + 1) {
                        logger.debug("année de début doit être entre 2022 et 2023 et l'écart entre debut et fin ne doit pas dépasser 1 ans");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                    }

                    existingMatiere.setDebut(updatedMatiere.getDebut());
                    existingMatiere.setFin(updatedMatiere.getFin());
                }

                if (updatedMatiere.getDebutime() != null && updatedMatiere.getFintime() != null) {
                    if (!updatedMatiere.getDebutime().isBefore(updatedMatiere.getFintime()) || updatedMatiere.getDebutime().isBefore(heureminimum)) {
                        logger.debug("format d'heure incorrecte: veuillez respecter le format: heuredebut <heurefin et heure debut>=09:00: ");
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST).build();
                    }

                    existingMatiere.setDebutime(updatedMatiere.getDebutime());
                    existingMatiere.setFintime(updatedMatiere.getFintime());
                }

                if (updatedMatiere.getJour() != null) {
                    String jourupdate = updatedMatiere.getJour().trim().toLowerCase();

                    if (!Arrays.asList(joursSemaineList).contains(jourupdate)) {
                        logger.debug("error entrer un jour valide");
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST).build();
                    }

                    existingMatiere.setJour(updatedMatiere.getJour());
                }

                if (updatedMatiere.getLocal() != null) {
                    String local = updatedMatiere.getLocal().trim();

                    if (!Pattern.matches(localPattern, local)) {
                        logger.debug("respecté le format suivant pour local: lettre suivit de chiffre ou lettre");
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST).build();
                    }

                    existingMatiere.setLocal(updatedMatiere.getLocal());
                }

                if (updatedMatiere.getSecondaire() != null) {
                    existingMatiere.setSecondaire(updatedMatiere.getSecondaire());
                }

                matiererepository.save(existingMatiere);

                logger.debug("réussite de la mise à jour des champs debut, fin, debutime, fintime pour la Matiere avec l'ID : " + id);
                return ResponseEntity.ok(existingMatiere);
            } else {
                logger.debug("Matiere avec l'ID : " + id + " n'a pas été trouvé");
                return ResponseEntity.notFound().build();
            }
        } else {
            logger.debug("Attention, vous n'avez pas la bonne permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    private static boolean hasAuthority(Authentication authentication, String expectedAuthority) {
        logger.debug("vérifier l'autorité de permission", expectedAuthority);
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(expectedAuthority));
    }
}
