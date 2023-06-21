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
    @PostMapping("/personnes")
    public ResponseEntity<Personne> addPersonne(@RequestBody Personne personne, UriComponentsBuilder builder) {
        // Gérer l'ajout de la personne en utilisant le modèle et le référentiel appropriés
        personne.setId(UUID.randomUUID()); // Générez un ID unique pour la personne

        // Enregistrez la personne dans votre base de données ou source de données
        personneRepository.save(personne);

        // Construire l'URI de la nouvelle personne créée
        URI location = builder.path("/add/personnes/{id}").buildAndExpand(personne.getId()).toUri();

        // Retourner une réponse avec l'URI de la nouvelle personne créée
        return ResponseEntity.created(location).body(personne);
    }



}

