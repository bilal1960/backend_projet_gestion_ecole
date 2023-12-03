package com.example.ecole.repository;

import com.example.ecole.models.Personne;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonneRepository extends JpaRepository<Personne, UUID> {
    Page<Personne> findAll(Pageable pageable);

    List<Personne> findAll();

    List<Personne> findAllByStatut(String statut);
    Optional<Personne> findByAuth0Id(String auth0Id);

}
