package com.example.ecole.repository;
import com.example.ecole.models.Ecole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EcoleRepository extends JpaRepository<Ecole,Long> {
    Page<Ecole> findAll(Pageable pageable);
}
