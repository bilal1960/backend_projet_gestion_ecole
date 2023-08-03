package com.example.ecole.repository;

import com.example.ecole.models.Inscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InscriptionRepository extends JpaRepository<Inscription, UUID> {
    @EntityGraph(attributePaths = "personne_id")
    Page<Inscription> findAll(Pageable pageable);
}
