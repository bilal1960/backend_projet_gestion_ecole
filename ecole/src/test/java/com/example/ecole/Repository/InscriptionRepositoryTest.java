package com.example.ecole.Repository;

import com.example.ecole.models.Inscription;
import com.example.ecole.models.Personne;
import com.example.ecole.repository.InscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDate;

@DataJpaTest
public class InscriptionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    Inscription inscription;
    Personne personne;
    Personne personnenotexist;

    private LocalDate date = LocalDate.of(2011, 9, 10);
    private LocalDate date_inscrit = LocalDate.of(2023, 9, 10);


    @BeforeEach
    public void setUp() {
        personne = new Personne("lolo", "laura", date, "Belge", "rue Test/70", "homme", "etudiant");
        inscription = new Inscription("Uccle", 500.0f, personne, date_inscrit, 50.0f, "General", "4 secondaire");
        personnenotexist = new Personne("laura", "Lulu", date, "Belge", "rue du parc/50", "femme", "etudiant");

    }

    @Test
    public void whenFindAll_thenPersonneIsLoaded() {

        entityManager.persist(personne);
        entityManager.persist(inscription);
        entityManager.flush();

        Page<Inscription> found = inscriptionRepository.findAll(PageRequest.of(0, 1));

        assertNotNull(found.getContent());
        assertNotNull(found.getContent().get(0).getPersonne());
        assertEquals(personne.getId(), found.getContent().get(0).getPersonne().getId());
        assertTrue(inscription.getPersonne().equals(personne));
    }

    @Test
    public void whenpageablenotexist() {

        Page<Inscription> found = inscriptionRepository.findAll(PageRequest.of(10, 1));

        assertTrue(found.getContent().isEmpty());

    }

    @Test
    public void personnewithInscription_empty() {

        entityManager.persist(personnenotexist);
        entityManager.flush();
        entityManager.remove(personnenotexist);

        Page<Inscription> found = inscriptionRepository.findAll(PageRequest.of(0, 1));

        assertTrue(found.getContent().isEmpty());
    }
}

