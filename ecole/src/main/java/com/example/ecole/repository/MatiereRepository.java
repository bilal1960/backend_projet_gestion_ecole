package com.example.ecole.repository;
import com.example.ecole.models.Matiere;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface MatiereRepository extends JpaRepository<Matiere, UUID> {

    Page<Matiere> findAll(Pageable pageable);

}
