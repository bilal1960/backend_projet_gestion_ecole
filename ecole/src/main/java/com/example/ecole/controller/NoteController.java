package com.example.ecole.controller;

import com.example.ecole.models.Note;
import com.example.ecole.models.Personne;
import com.example.ecole.repository.NoteRepository;
import com.example.ecole.repository.PersonneRepository;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/add/note")
public class NoteController {

    private static final Logger logger = LoggerFactory.getLogger(Note.class);

    @Autowired
    NoteRepository noteRepository;

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

    public NoteController(NoteRepository noteRepository, PersonneRepository personneRepository) {
        this.noteRepository = noteRepository;
        this.personneRepository = personneRepository;
    }

    @GetMapping
    public ResponseEntity<List<Note>> getAllNote(Pageable pageable, Authentication authentication) {

        if (hasAuthority(authentication, "SCOPE_read:personne")) {
            List<Note> notes = noteRepository.findAll();
            return ResponseEntity.ok(notes);
        } else {

            logger.debug("il y a eu un problème de récupération");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/notepersonne")
    public ResponseEntity<List<Personne>> getAllNotestudent(Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_read:note")) {
            List<Personne> personness = personneRepository.findAllByStatut("etudiant");
            logger.debug("succès de l'affichage de la liste personne");

            return ResponseEntity.ok(personness);
        } else {
            logger.debug("échec mauvaise permission ");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/paginote")
    public ResponseEntity<Page<Note>> getPaginatedNote(Pageable pageable, Authentication authentication) {
        Page<Note> notes = noteRepository.findAll(pageable);
        return ResponseEntity.ok(notes);
    }

    @PostMapping
    public ResponseEntity<?> addNote(@RequestBody Note note, UriComponentsBuilder builder, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_write:note")) {
            Personne personne = personneRepository.findById(note.getPersonne().getId()).orElse(null);

            String emailTo = personne.getMail();
            String studentName = personne.getPrenom() + " " + personne.getNom();
            String matiereName = note.getNom();
            LocalDate deliberation = note.getDeliberation();
            String session = note.getSession();
            boolean reussi = note.isReussi();
            double resultat = note.getResultat();

            boolean isDateInEvents = eventDates.stream().anyMatch(dates ->
                    (deliberation.isEqual(dates[0]) || deliberation.isAfter(dates[0])) &&
                            (deliberation.isEqual(dates[1]) || deliberation.isBefore(dates[1]))
            );

            if (isDateInEvents) {
                logger.debug("Échec de la validation de la date (pendant les événements)");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("erreur", "La date tombe pendant des vacances ou jour férié, sélectionné une autre"));
            }

            if(deliberation.getDayOfWeek() == DayOfWeek.SATURDAY || deliberation.getDayOfWeek() == DayOfWeek.SUNDAY){
                logger.debug("Échec de la validation de la date le week-end");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("erreur", "On ne peut pas encoder une note un week-end"));

            }



            if(resultat <0|| resultat >100){

                logger.debug("Le résultat ne peut être négatif ou supérieur à 100.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("erreur", "Le résultat ne peut être négatif ou supérieur à 100"));
            }

            if(!session.equals("1 session") && !session.equals("2 session")){

                logger.debug("donnée de saisit incorrecte: entrer 1 session ou 2 session");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("erreur", "encodé 1 session ou 2 session"));

            }

            if(resultat <50 && reussi ){

                logger.debug("resultat <= 50%");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("erreur", "une note inférieur à 50% ne peut pas être réussi"));
            }

            if (resultat >= 50 && !reussi) {
                logger.debug("Incohérence détectée : un résultat de 50% ou plus doit être marqué comme réussi.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("erreur", "Un résultat de 50% ou plus doit être considéré comme une réussite."));
            }

            if (personne != null) {
                List<Note> notesActuelles = personne.getNotes();
                if (notesActuelles == null) {
                    notesActuelles = new ArrayList<>();
                }
                notesActuelles.add(note);
                personne.setNotes(notesActuelles);

                personne.calculerMoyenne();

                note.setPersonne(personne);
                noteRepository.save(note);
                personneRepository.save(personne);

                URI location = builder.path("/add/note/{id}").buildAndExpand(note.getId()).toUri();


                emailService.sendStudentResultEmail(emailTo, studentName, matiereName, deliberation, session, reussi, resultat);

                logger.debug("Succès de l'ajout des données ");

                return ResponseEntity.created(location).body(note);
            } else {
                logger.debug("Échec de la création de la note : personne inexistante.");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

        } else {

            logger.debug("Échec de la création de la note : personne inexistante.");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        }




    private static boolean hasAuthority(Authentication authentication, String expectedAuthority) {
        logger.debug("vérifier l'autorité de permission", expectedAuthority);
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(expectedAuthority));
    }

}
