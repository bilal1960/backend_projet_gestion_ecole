package com.example.ecole.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/add/permissions")
public class PermissionApiController {

    Logger logger = LoggerFactory.getLogger(PermissionApiController.class);

    @GetMapping
    public List<String> getPermissions(Authentication authentication) {
        logger.debug("l'utilisateur ne dispose pas des bons droits d'accès");

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map((a) -> a.replace("SCOPE_", ""))
                .toList();
    }
}
