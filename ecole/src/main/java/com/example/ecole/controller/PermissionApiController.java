package com.example.ecole.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequestMapping("/add/permissions")
public class PermissionApiController {

    @GetMapping
    public List<String> getPermissions(Authentication authentication) {
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).map((a) -> a.replace("SCOPE_", "")).toList();
    }

    @PostMapping
    public ResponseEntity<String> checkWriteMatierePermission(Authentication authentication) {
        if (hasAuthority(authentication, "SCOPE_write:matiere")) {
            // L'utilisateur a l'autorisation "SCOPE_write:matiere", donc il est autorisé à créer une matière
            return ResponseEntity.ok("Authorized to create matiere");
        } else {
            // L'utilisateur n'a pas les autorisations nécessaires pour créer une matière
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized to create matiere");
        }
    }

    private boolean hasAuthority(Authentication authentication, String requiredAuthority) {
        return authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals(requiredAuthority));
    }

}
