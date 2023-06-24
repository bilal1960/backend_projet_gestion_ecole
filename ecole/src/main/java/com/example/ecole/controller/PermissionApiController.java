package com.example.ecole.controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
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
}
