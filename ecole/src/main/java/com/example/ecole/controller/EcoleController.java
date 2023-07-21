package com.example.ecole.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.ecole.repository.EcoleRepository;
import com.example.ecole.models.Ecole;
import java.util.List;


@RestController
@RequestMapping("/ecoles")
public class EcoleController {
    @Autowired
    private final EcoleRepository ecoleRepository;
    private static final Logger logger = LoggerFactory.getLogger(EcoleController.class);

    public EcoleController(EcoleRepository ecoleRepository) {
        this.ecoleRepository = ecoleRepository;
    }

@GetMapping
    public ResponseEntity<List<Ecole>> getAllEcoles(Authentication authentication) {
        List<Ecole> ecoles = ecoleRepository.findAll();
        logger.info("succès de l'affichage de la liste");
        return ResponseEntity.ok(ecoles);
    }
}



