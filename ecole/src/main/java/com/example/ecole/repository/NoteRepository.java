package com.example.ecole.repository;

import com.example.ecole.models.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NoteRepository extends JpaRepository<Note, UUID> {

    Page<Note> findAll(Pageable pageable);
    List<Note> finAll();


}
