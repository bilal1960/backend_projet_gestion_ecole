package com.example.ecole.controller;
import com.example.ecole.models.PaginatedMatiereResponse;
import com.example.ecole.repository.MatiereRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import com.example.ecole.models.Matiere;
import org.slf4j.LoggerFactory;



@RestController
@RequestMapping("/add")
public class MatiereController {
    private static final Logger logger = LoggerFactory.getLogger(MatiereController.class);
    @Autowired
    private  MatiereRepository matiererepository;

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
    @PostMapping("/matieres")
    public ResponseEntity<Matiere> addMatiere(@RequestBody Matiere matiere, UriComponentsBuilder builder, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_write:matiere")) {
            matiere.setId(UUID.randomUUID());
            matiererepository.save(matiere);
            URI location = builder.path("/add/matieres/{id}").buildAndExpand(matiere.getId()).toUri();
            logger.info("succès de la sauvegarde des données dans la database");
            return ResponseEntity.created(location).body(matiere);
        }
             logger.warn("attention vous n'avez pas la bonne permission");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();


    }

    @GetMapping("/matieres/api")
    public ResponseEntity<PaginatedMatiereResponse> getPaginatedMatieres(Authentication authentication,
                                                                         @RequestParam int page, @RequestParam int size) {
        if (hasAuthority(authentication, "SCOPE_read:matiere")) {
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
            Page<Matiere> matierePage = matiererepository.findAll(pageRequest);
            List<Matiere> matieres = matierePage.getContent();

            // Créer l'objet PaginatedMatiereResponse avec le contenu des matières et les informations de pagination
            PaginatedMatiereResponse response = new PaginatedMatiereResponse(matieres, matierePage.getTotalPages(), matierePage.getTotalElements());
            logger.info("succès de la pagination de la liste des matières");
            return ResponseEntity.ok(response);
        } else {
            logger.warn("erreur de la pagination de la liste, vérifier vos permissions");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    private static boolean hasAuthority(Authentication authentication, String expectedAuthority) {
        logger.info("vérifier l'autorité de permission", expectedAuthority);
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(expectedAuthority));
    }
}
