package com.example.ecole.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Ecole> getEcole(Authentication authentication) {
        UUID id = UUID.fromString("04ece6ce-2690-4152-a5c9-09d40d5891b7");

        Ecole ecole = ecoleRepository.findEcoleById(id); // Assurez-vous d'avoir une méthode pour récupérer l'école unique.
        if (ecole != null) {
            logger.info("Succès de l'affichage de l'école");
            return ResponseEntity.ok(ecole);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}



