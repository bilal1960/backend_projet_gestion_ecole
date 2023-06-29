package com.example.ecole.controller;
import com.example.ecole.models.Inscription;
import com.example.ecole.repository.InscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;


@RestController
@RequestMapping("/add/inscriptions")
public class InscriptionController {

    @Autowired
    private InscriptionRepository inscritRepository;

    public InscriptionController(InscriptionRepository inscriptionRepository) {
        this.inscritRepository = inscriptionRepository;
    }

    @GetMapping
    public ResponseEntity<List<Inscription>> getAllInscription(Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_read:inscrit")) {
            List<Inscription> inscriptions = inscritRepository.findAll();

            return ResponseEntity.ok(inscriptions);
        }else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }
    }

    @PostMapping
    public ResponseEntity<Inscription> addInscrit(@RequestBody Inscription inscription, UriComponentsBuilder builder, Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_write:inscrit")) {
            inscription.setId(UUID.randomUUID());
            inscritRepository.save(inscription);
            URI location = builder.path("/add/inscriptions/{id}").buildAndExpand(inscription.getId()).toUri();
            return ResponseEntity.created(location).body(inscription);
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();


        }


    }

    private static boolean hasAuthority(Authentication authentication, String expectedAuthority) {
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(expectedAuthority));
    }



}


