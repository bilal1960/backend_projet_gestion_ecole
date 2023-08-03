package com.example.ecole.repository;

import com.example.ecole.models.Matiere;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MatiereRepository extends JpaRepository<Matiere, UUID> {
    @EntityGraph(attributePaths = "professeur_id")
    Page<Matiere> findAll(Pageable pageable);
}
