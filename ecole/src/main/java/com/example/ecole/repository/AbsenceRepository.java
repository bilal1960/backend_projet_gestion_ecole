package com.example.ecole.repository;

import com.example.ecole.models.Absence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AbsenceRepository extends JpaRepository<Absence, UUID> {
    @EntityGraph(attributePaths = "personne_idpresence")
    Page<Absence> findAll(Pageable pageable);
}
