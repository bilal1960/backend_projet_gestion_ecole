package com.example.ecole.controller;
import com.example.ecole.models.PaginatedMatiereResponse;
import com.example.ecole.repository.MatiereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import org.springframework.data.domain.Pageable;



@RestController
@RequestMapping("/add")
public class MatiereController {
    @Autowired
    private  MatiereRepository matiererepository;

    public MatiereController(MatiereRepository matiererepository) {
        this.matiererepository = matiererepository;
    }

    @GetMapping("/matieres")

    public ResponseEntity<List<Matiere>> getAllMatieres(Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_read:matiere")) {
            List<Matiere> matieres = matiererepository.findAll();
            return ResponseEntity.ok(matieres);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    @PostMapping("/matieres")
    public ResponseEntity<Matiere> addMatiere(@RequestBody Matiere matiere, UriComponentsBuilder builder, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_write:matiere")) {
            matiere.setId(UUID.randomUUID());
            matiererepository.save(matiere);
            URI location = builder.path("/add/matieres/{id}").buildAndExpand(matiere.getId()).toUri();
            return ResponseEntity.created(location).body(matiere);
        }
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

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }




    private static boolean hasAuthority(Authentication authentication, String expectedAuthority) {
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(expectedAuthority));
    }




}
