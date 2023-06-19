package com.example.ecole.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.ecole.repository.EcoleRepository;
import com.example.ecole.models.Ecole;
import org.springframework.data.domain.Sort;


@RestController
@RequestMapping("/ecoles")
public class EcoleController {
    private final EcoleRepository ecoleRepository;

    public EcoleController(EcoleRepository ecoleRepository) {
        this.ecoleRepository = ecoleRepository;
    }


    @GetMapping("/pagi")
    public Page<Ecole> getEcolesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "nombreEtudiants") String sortBy

    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy));
        return ecoleRepository.findAll(pageRequest);
    }


    @RequestMapping("/error")
    public String handleError() {
        // Gérer l'erreur ici et retourner une réponse appropriée
        return "Erreur lors de la récupération des écoles";
    }

}

