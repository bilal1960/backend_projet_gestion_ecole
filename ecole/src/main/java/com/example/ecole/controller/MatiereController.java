package com.example.ecole.controller;
import com.example.ecole.repository.MatiereRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import com.example.ecole.models.Matiere;
@RestController
@RequestMapping("/add")
public class MatiereController {

    private final MatiereRepository matiererepository;

    public MatiereController(MatiereRepository matiererepository) {
        this.matiererepository = matiererepository;
    }

    @GetMapping("/matieres")
    public ResponseEntity<List<Matiere>> getAllMatiere(Authentication authentication) {
        // Récupérer les données de votre base de données ou de votre source de données
        List<Matiere> matieres = matiererepository.findAll();

        // Retourner les données sous forme de ResponseEntity
        return ResponseEntity.ok(matieres);
    }

    @PostMapping("/matieres")
    public ResponseEntity<Matiere> addMatiere(@RequestBody Matiere matiere, UriComponentsBuilder builder) {
        // Gérer l'ajout de la personne en utilisant le modèle et le référentiel appropriés
        matiere.setId(UUID.randomUUID()); // Générez un ID unique pour la personne

        // Enregistrez la personne dans votre base de données ou source de données
        matiererepository.save(matiere);

        // Construire l'URI de la nouvelle personne créée
        URI location = builder.path("/add/matieres/{id}").buildAndExpand(matiere.getId()).toUri();

        // Retourner une réponse avec l'URI de la nouvelle personne créée
        return ResponseEntity.created(location).body(matiere);

    }
}
