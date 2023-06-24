package com.example.ecole.controller;
import com.example.ecole.repository.MatiereRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import com.example.ecole.models.Matiere;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasAuthority;

@RestController
@RequestMapping("/add")
public class MatiereController {

    private final MatiereRepository matiererepository;

    public MatiereController(MatiereRepository matiererepository) {
        this.matiererepository = matiererepository;
    }

    @GetMapping("/matieres")

    public ResponseEntity<List<Matiere>> getAllMatieres(Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_read:all-matiere")) {
            // L'utilisateur a l'autorisation "read:all-matiere", donc il est autorisé à accéder à la liste des matières
            List<Matiere> matieres = matiererepository.findAll();
            return ResponseEntity.ok(matieres);
        } else {
            // L'utilisateur n'a pas les autorisations nécessaires
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    private boolean hasAuthority(Authentication authentication, String requiredAuthority) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(requiredAuthority));
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
