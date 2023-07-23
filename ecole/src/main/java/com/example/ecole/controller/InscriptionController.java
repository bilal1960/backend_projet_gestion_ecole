package com.example.ecole.controller;
import com.example.ecole.models.Inscription;
import com.example.ecole.models.Personne;
import com.example.ecole.repository.InscriptionRepository;
import com.example.ecole.repository.MatiereRepository;
import com.example.ecole.repository.PersonneRepository;
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
import org.slf4j.Logger;
@RestController
@RequestMapping("/add/inscriptions")
public class InscriptionController {
    private static final   Logger logger = LoggerFactory.getLogger(InscriptionController.class);
    @Autowired
    private InscriptionRepository inscritRepository;
    @Autowired
    private  PersonneRepository personneRepository;
    @Autowired
    private MatiereRepository   matiereRepository;
    public InscriptionController(InscriptionRepository inscriptionRepository) {
        this.inscritRepository = inscriptionRepository;
    }
    @GetMapping
    public ResponseEntity<List<Inscription>> getAllInscription(Pageable pageable,Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_read:inscrit")) {
            List<Inscription> inscriptions = inscritRepository.findAll();
            logger.info("la récupération est un succès");
            return ResponseEntity.ok(inscriptions);
        }else {
            logger.warn("il y a eu un problème de récupération");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }
    }
    @GetMapping("/api")
    public ResponseEntity<Page<Inscription>> getPaginatedInscriptions(Pageable pageable, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_read:inscrit")) {
            Page<Inscription> inscriptions = inscritRepository.findAll(pageable);
            logger.info("La récupération paginée est un succès");
            return ResponseEntity.ok(inscriptions);
        } else {
            logger.warn("Il y a eu un problème de récupération");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    @PostMapping
    public ResponseEntity<Inscription> addInscrit(@RequestBody Inscription inscription, UriComponentsBuilder builder, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_write:inscrit")) {
            Personne personne = personneRepository.findById(inscription.getPersonne().getId()).orElse(null);
            if (personne != null) {
                inscription.setPersonne(personne);
                inscription.setId(UUID.randomUUID());
                inscritRepository.save(inscription);
                URI location = builder.path("/add/inscriptions/{id}").buildAndExpand(inscription.getId()).toUri();
                logger.info("Succès de l'ajout des données ");
                return ResponseEntity.created(location).body(inscription);
            } else {
                logger.warn("La personne correspondante n'a pas été trouvée");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } else {
            logger.warn("Échec mauvaise permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    private static boolean hasAuthority(Authentication authentication, String expectedAuthority) {
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(expectedAuthority));
    }
}


