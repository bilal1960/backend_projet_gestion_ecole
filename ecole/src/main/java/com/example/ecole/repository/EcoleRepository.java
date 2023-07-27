package com.example.ecole.repository;
import com.example.ecole.models.Ecole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EcoleRepository extends JpaRepository<Ecole, UUID> {
    Page<Ecole> findAll(Pageable pageable);
    Ecole findEcoleById(UUID id);

}
