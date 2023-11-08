package com.example.ecole.controller;

import com.example.ecole.models.Vacance;
import com.example.ecole.repository.VacanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/add/vac")
public class VacanceController {

    @Autowired
    private VacanceRepository vacanceRepository;

    private static final Logger logger = LoggerFactory.getLogger(VacanceController.class);

    public VacanceController(VacanceRepository vacanceRepository) {

        this.vacanceRepository = vacanceRepository;
    }

    @GetMapping("/vacance")
    public ResponseEntity<List<Vacance>> getAllVacance(Authentication authentication) {

        if (hasAuthority(authentication,"SCOPE_read:vacance")){

            List<Vacance> vacances = vacanceRepository.findAll();

            return ResponseEntity.ok(vacances);
        }else{

            logger.debug("échec mauvaise permission ");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
    }






    private static boolean hasAuthority(Authentication authentication, String expectedAuthority) {
        logger.debug("vérifier l'autorité de permission", expectedAuthority);
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(expectedAuthority));
    }
}
