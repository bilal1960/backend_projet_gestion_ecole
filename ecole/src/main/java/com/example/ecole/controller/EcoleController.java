package com.example.ecole.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.ecole.repository.EcoleRepository;
import com.example.ecole.models.Ecole;
import java.util.UUID;
@RestController
@RequestMapping("/add/ecoles")
public class EcoleController {
    @Autowired
    private final EcoleRepository ecoleRepository;
    private static final Logger logger = LoggerFactory.getLogger(EcoleController.class);

    public EcoleController(EcoleRepository ecoleRepository) {
        this.ecoleRepository = ecoleRepository;
    }

    @GetMapping("/ecole")
    public ResponseEntity<Ecole> getEcole() {
        UUID id = UUID.fromString("04ece6ce-2690-4152-a5c9-09d40d5891b7");

        Ecole ecole = ecoleRepository.findEcoleById(id); // Assurez-vous d'avoir une méthode pour récupérer l'école unique.
        if (ecole != null) {
            logger.debug("Succès de l'affichage de l'école");
            return ResponseEntity.ok(ecole);
        } else {
            logger.debug("problème affichage data vérifier la DB");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/ecole/{id}")
    public ResponseEntity<Ecole> updateEcole(@PathVariable UUID id, @RequestBody Ecole updatedEcole, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_write:ecole")) {

            Ecole existingEcole = ecoleRepository.findEcoleById(id);
            if (existingEcole == null) {
                logger.debug("Échec, école introuvable");
                return ResponseEntity.notFound().build();
            }

            if (updatedEcole.getAdresse() != null && updatedEcole.getAdresse().isEmpty()) {
                existingEcole.setAdresse(updatedEcole.getAdresse());
            }
            if(updatedEcole.getMail() != null && updatedEcole.getMail().isEmpty()){
                existingEcole.setMail(updatedEcole.getMail());
            }
            if(updatedEcole.getNumber() != null && updatedEcole.getNumber().isEmpty()){
                existingEcole.setNumber(updatedEcole.getNumber());

            }
            if(updatedEcole.getType() != null && updatedEcole.getType().isEmpty()){
                existingEcole.setType(updatedEcole.getType());
            }

            Ecole updatedSchool = ecoleRepository.save(existingEcole);
            logger.debug("Succès de la mise à jour de l'école");
            return ResponseEntity.ok(updatedSchool);
        }
        logger.debug("Attention, vous n'avez pas la bonne permission");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private static boolean hasAuthority(Authentication authentication, String expectedAuthority) {
        logger.debug("vérifier l'autorité de permission", expectedAuthority);
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(expectedAuthority));
    }

}



