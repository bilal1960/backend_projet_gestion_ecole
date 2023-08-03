package com.example.ecole.controller;

import com.example.ecole.models.Personne;
import com.example.ecole.repository.MatiereRepository;
import com.example.ecole.repository.PersonneRepository;
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
    int currentYear = Year.now().getValue();
    LocalTime heureminimum = LocalTime.of(9, 0);
    String[] joursSemaineList = {"lundi", "mardi", "mercredi", "jeudi", "vendredi"};
    String localPattern = "^[a-zA-Z][a-zA-Z0-9]*$"; // Le pattern oblige une lettre au début suivit d'éventuelle lettre ou chiffre positif sans aucun espace


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
        int finYear = debutYear + 1;
        List<Matiere> matieress = new ArrayList<>();

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
                matiere.setPersonne(professeur);
                matiererepository.save(matiere);
                matieress.add(matiere);
                professeur.setMatieres(matieress);
                personneRepository.save(professeur);
                URI location = builder.path("/add/matieres/{id}").buildAndExpand(matiere.getId()).toUri();
                logger.debug("succès de la sauvegarde des données dans la base de données");
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

            if (existingMatiereOptional.isPresent()) {
                Matiere existingMatiere = existingMatiereOptional.get();

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
