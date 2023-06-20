package com.example.ecole.controller;
import com.example.ecole.models.Personne;
import com.example.ecole.repository.PersonneRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/add")
public class PersonneAjouterController {

    private final PersonneRepository personneRepository;

    public PersonneAjouterController(PersonneRepository personneRepository) {
        this.personneRepository = personneRepository;
    }

    @GetMapping("/personnes")
    public ResponseEntity<List<Personne>> getAllPersonnes(Authentication authentication) {
        // Récupérer les données de votre base de données ou de votre source de données
        List<Personne> personnes = personneRepository.findAll();

        // Retourner les données sous forme de ResponseEntity
        return ResponseEntity.ok(personnes);
    }

    // Autres méthodes pour ajouter, supprimer ou mettre à jour les données

}

