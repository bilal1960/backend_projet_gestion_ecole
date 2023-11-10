package com.example.ecole.controller;

import com.example.ecole.models.Personne;
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
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/add/perso")
public class PersonneAjouterController {

    private static final Logger logger = LoggerFactory.getLogger(PersonneAjouterController.class);
    @Autowired
    private final PersonneRepository personneRepository;

    public PersonneAjouterController(PersonneRepository personneRepository) {
        this.personneRepository = personneRepository;
    }

    @GetMapping("/api")
    public ResponseEntity<List<Personne>> getAllPersonnes(Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_read:personne")) {
            List<Personne> personness = personneRepository.findAllByStatut("etudiant");
            logger.debug("succès de l'affichage de la liste personne");

            return ResponseEntity.ok(personness);
        } else {
            logger.debug("échec mauvaise permission ");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/professeurs/api")
    public ResponseEntity<List<Personne>> getAllProfesseurs(Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_read:personne")) {
            List<Personne> professeurs = personneRepository.findAllByStatut("professeur");
            logger.debug("Succès de l'affichage de la liste des profs");
            return ResponseEntity.ok(professeurs);
        } else {
            logger.debug("Échec, mauvaise permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    @GetMapping("/api1")
    public ResponseEntity<Page<Personne>> getPaginatedIPersonne(Pageable pageable, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_read:personne")) {
            Page<Personne> personnes = personneRepository.findAll(pageable);
            logger.debug("La récupération paginée est un succès");
            return ResponseEntity.ok(personnes);
        } else {
            logger.debug("Il y a eu un problème de récupération");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/pagi")
    public ResponseEntity<Personne> addPersonne(@RequestBody Personne personne, Authentication authentication, UriComponentsBuilder builder) {

        if (hasAuthority(authentication, "SCOPE_write:personne")) {

            personne.setId(UUID.randomUUID());
            personneRepository.save(personne);
            URI location = builder.path("/add/personnes/{id}").buildAndExpand(personne.getId()).toUri();
            logger.debug("succès de la sauvegarde des données dans la database");
            return ResponseEntity.created(location).body(personne);
        }
        logger.debug("attention vous n'avez pas la bonne permission");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/persos/{id}")
    public ResponseEntity<Personne> updatePersonne(@PathVariable UUID id, @RequestBody Personne updatedPersonne, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_write:personne")) {
            Personne existingPersonne = personneRepository.findById(id).orElse(null);

            if (existingPersonne == null) {
                logger.debug("id de la personne est inexistant");
                return ResponseEntity.notFound().build();
            }

            existingPersonne.setAdresse(updatedPersonne.getAdresse());

            personneRepository.save(existingPersonne);

            logger.debug("Succès de la mise à jour des données dans la database");

            return ResponseEntity.ok(existingPersonne);
        }

        logger.debug("Attention, vous n'avez pas la bonne permission");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private static boolean hasAuthority(Authentication authentication, String expectedAuthority) {
        logger.debug("vérifier l'autorité de permission", expectedAuthority);
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(expectedAuthority));
    }
}



