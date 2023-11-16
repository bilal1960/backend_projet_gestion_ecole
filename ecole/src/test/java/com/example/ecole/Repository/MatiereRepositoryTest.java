package com.example.ecole.Repository;

import com.example.ecole.models.Matiere;
import com.example.ecole.models.Personne;
import com.example.ecole.repository.MatiereRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDate;
import java.time.LocalTime;

@DataJpaTest
public class MatiereRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MatiereRepository matiereRepository;

    LocalDate datedbut = LocalDate.of(2023, 9, 10);
    LocalDate datefin = LocalDate.of(2024, 1, 10);
    LocalTime debutime = LocalTime.of(9, 0);
    LocalTime fintime = LocalTime.of(16, 30);
    LocalDate birthDate = LocalDate.now().minusYears(21);


    Matiere matiere;
    Personne professeur;
    Personne professeurNotExist;

    @BeforeEach
    public void setUp() {
        professeur = new Personne("lolo", "laura", birthDate, "Belge", "rue du royal/50", "homme", "professeur",0.0f,"prof@hotmail.com");
        matiere = new Matiere("Mathématiques", datedbut, datefin, professeur, debutime, fintime, "A400", "lundi", "3 secondaire");
        professeurNotExist = new Personne("lolo", "laura", birthDate, "Belge", "rue Test/80", "homme", "professeur",0.0f,"proffesseur@hotmail.com");
    }

    @Test
    public void whenFindAll_thenProfesseurIsLoaded() {
        entityManager.persist(professeur);
        entityManager.persist(matiere);
        entityManager.flush();

        Page<Matiere> found = matiereRepository.findAll(PageRequest.of(0, 1));

        assertNotNull(found.getContent());
        assertNotNull(found.getContent().get(0).getPersonne());
        assertEquals(professeur.getId(), found.getContent().get(0).getPersonne().getId());
    }

    @Test
    public void whenFindAllWithNonExistingPage_thenContentIsEmpty() {
        Page<Matiere> found = matiereRepository.findAll(PageRequest.of(10, 1));
        assertTrue(found.getContent().isEmpty());
    }

    @Test
    public void whenFindAllWithNonExistingProfesseur_thenContentIsEmpty() {
        entityManager.persist(professeurNotExist);
        entityManager.flush();
        entityManager.remove(professeurNotExist);

        Page<Matiere> found = matiereRepository.findAll(PageRequest.of(0, 1));
        assertTrue(found.getContent().isEmpty());
    }
    @Test
    public  void whenpageablenotexisting(){

        Page<Matiere> found = matiereRepository.findAll(PageRequest.of(10, 1));

        assertTrue(found.getContent().isEmpty());
    }
}

