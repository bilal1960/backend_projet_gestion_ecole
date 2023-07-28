package com.example.ecole.controller;
import com.example.ecole.models.Personne;
import com.example.ecole.repository.MatiereRepository;
import com.example.ecole.repository.PersonneRepository;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
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
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import com.example.ecole.models.Matiere;
import org.slf4j.LoggerFactory;



@RestController
@RequestMapping("/add/matiere")
public class MatiereController {
    private static final Logger logger = LoggerFactory.getLogger(MatiereController.class);
    @Autowired
    private  MatiereRepository matiererepository;
    @Autowired
    private PersonneRepository personneRepository;



    public MatiereController(MatiereRepository matiererepository) {
        this.matiererepository = matiererepository;
    }
    @GetMapping("/matieres")
    public ResponseEntity<List<Matiere>> getAllMatieres(Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_read:matiere")) {
            List<Matiere> matieres = matiererepository.findAll();
            logger.info("succès de l'affichage de la liste");
            return ResponseEntity.ok(matieres);
        } else {
            logger.warn("échec mauvaise permission ");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
   @PostMapping("/matieress")
   public ResponseEntity<?> addMatiere(@RequestBody Matiere matiere, UriComponentsBuilder builder, Authentication authentication) {
            LocalTime heureminimum = LocalTime.of(9, 0);
        if (hasAuthority(authentication, "SCOPE_write:matiere")) {
           Personne professeur = personneRepository.findById(matiere.getPersonne().getId()).orElse(null);
           if (professeur != null) {
               if (!matiere.getDebutime().isBefore(matiere.getFintime() ) || matiere.getDebutime().isBefore(heureminimum) ) {
                   return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
               }
               if(!matiere.getDebut().isBefore(matiere.getFin())){
                   return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
               }
               matiere.setPersonne(professeur);
               matiere.setId(UUID.randomUUID());
               matiererepository.save(matiere);
               URI location = builder.path("/add/matieres/{id}").buildAndExpand(matiere.getId()).toUri();
               logger.info("succès de la sauvegarde des données dans la base de données");
               return ResponseEntity.created(location).body(matiere);
           } else {
               logger.warn("Le professeur correspondant n'a pas été trouvé");
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
           }
       } else {
           logger.warn("Attention, vous n'avez pas la bonne permission");
           return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
       }
   }
    @GetMapping("/matieres/api")
    public ResponseEntity<Page<Matiere>> getPaginatedMatieres(Pageable pageable, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_read:matiere")) {
            Page<Matiere> matieres = matiererepository.findAll(pageable);
            logger.info("La récupération paginée est un succès");
            return ResponseEntity.ok(matieres);
        } else {
            logger.warn("Il y a eu un problème de récupération");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/professeurs/api1")
    public ResponseEntity<List<Personne>> getAllProfesseurs(Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_read:matiere")) {
            List<Personne> professeurs = personneRepository.findAllByStatut("professeur");
            logger.info("Succès de l'affichage de la liste des profs");
            return ResponseEntity.ok(professeurs);
        } else {
            logger.warn("Échec, mauvaise permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    @PutMapping("/matieres/{id}")
    public ResponseEntity<?> updateMatiere(@PathVariable UUID id, @RequestBody Matiere updatedMatiere, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_write:matiere")) {
            Optional<Matiere> existingMatiereOptional = matiererepository.findById(id);
            LocalTime debutime = LocalTime.of(9,0);
            String[] joursSemaineList = {"lundi", "mardi", "mercredi", "jeudi", "vendredi"};
            String jourupdate = updatedMatiere.getJour().toLowerCase();
            String localPattern = "^[a-zA-Z][a-zA-Z0-9]*$"; // Le pattern oblige une lettre au début suivit d'éventuelle lettre ou chiffre positif sans aucun espace
            String local = updatedMatiere.getLocal();

            if (existingMatiereOptional.isPresent()) {
                Matiere existingMatiere = existingMatiereOptional.get();

                if (updatedMatiere.getFin().isBefore(updatedMatiere.getDebut())|| updatedMatiere.getDebut().isBefore(existingMatiere.getDebut())|| updatedMatiere.getFin().isBefore(existingMatiere.getFin())) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("La date de début doit être antérieure à la date de fin.");
                }

                if(!updatedMatiere.getDebutime().isBefore(updatedMatiere.getFintime())|| updatedMatiere.getDebutime().isBefore(debutime)){
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("la date de debut doit être supérieure à 09:00");
                }
                if (!Arrays.asList(joursSemaineList).contains(jourupdate) || !Pattern.matches(localPattern, local)) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("Le jour doit être lundi, mardi, mercredi, jeudi ou vendredi.");
                }

                existingMatiere.setDebut(updatedMatiere.getDebut());
                existingMatiere.setFin(updatedMatiere.getFin());
                existingMatiere.setDebutime(updatedMatiere.getDebutime());
                existingMatiere.setFintime(updatedMatiere.getFintime());
                existingMatiere.setLocal(updatedMatiere.getLocal());
                existingMatiere.setJour(updatedMatiere.getJour());
                matiererepository.save(existingMatiere);

                logger.info("réussite de la mise à jour des champs debut, fin, debutime, fintime pour la Matiere avec l'ID : " + id);
                return ResponseEntity.ok(existingMatiere);
            } else {
                logger.warn("Matiere avec l'ID : " + id + " n'a pas été trouvé");
                return ResponseEntity.notFound().build();
            }
        } else {
            logger.warn("Attention, vous n'avez pas la bonne permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    private static boolean hasAuthority(Authentication authentication, String expectedAuthority) {
        logger.info("vérifier l'autorité de permission", expectedAuthority);
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(expectedAuthority));
    }
}
