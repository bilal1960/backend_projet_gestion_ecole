package com.example.ecole.repository;

import com.example.ecole.models.Vacance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VacanceRepository extends JpaRepository<Vacance, UUID> {

    Page<Vacance> findAll(Pageable pageable);

    List<Vacance> findAll();
}
