package com.example.ecole.controller;

import com.example.ecole.models.Inscription;
import com.example.ecole.models.Note;
import com.example.ecole.models.Personne;
import com.example.ecole.repository.NoteRepository;
import com.example.ecole.repository.PersonneRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/add/note")
public class NoteController {

    private static final Logger logger = LoggerFactory.getLogger(Note.class);

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    private PersonneRepository personneRepository;

    public NoteController(NoteRepository noteRepository, PersonneRepository personneRepository) {
        this.noteRepository = noteRepository;
        this.personneRepository = personneRepository;
    }

    @GetMapping
    public ResponseEntity<List<Note>> getAllNote(Pageable pageable, Authentication authentication) {

        if (hasAuthority(authentication, "SCOPE_read:note")) {
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
    public ResponseEntity<Note> addNote(@RequestBody Note note, UriComponentsBuilder builder, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_write:note")) {
            Personne personne = personneRepository.findById(note.getPersonne().getId()).orElse(null);

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
