package com.example.ecole.Repository;

import com.example.ecole.models.Personne;
import com.example.ecole.repository.PersonneRepository;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class PersonneRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonneRepository personneRepository;

    Personne personne1;
    Personne personne2;
    Personne personneerror;
    Personne personnewithnotargument;
    LocalDate birthDate = LocalDate.now().minusYears(21);
    LocalDate birthDateinscrit = LocalDate.now().minusYears(13);


    @BeforeEach
    public void setUp() {
        personne1 = new Personne("lolo", "laura", birthDate, "Belge", "rue Test/50", "homme", "professeur");
        personne2 = new Personne("laura", "lulu", birthDateinscrit, "Belge", "rue Test2/123", "homme", "etudiant");
        personneerror = new Personne(null, "laura", birthDate, "Belge", "rue Test/123", "homme", "professeur");
        personnewithnotargument = new Personne();
    }

    @Test
    public void whenFindById_thenReturnPersonne() {

        entityManager.persist(personne1);
        entityManager.flush();

        Personne found = personneRepository.findById(personne1.getId()).orElse(null);

        assertNotNull(found, "L'objet trouvé ne doit pas être nul.");
        assertEquals(personne1.getNom(), found.getNom(), "Le nom de la personne trouvée doit être égal au nom de personne1.");

    }

    @Test
    public void whenFindAllByStatut_thenReturnPersonnes() {

        entityManager.persist(personne1);

        entityManager.persist(personne2);
        entityManager.flush();

        List<Personne> foundteacher = personneRepository.findAllByStatut("professeur");
        List<Personne> foundstudent = personneRepository.findAllByStatut("etudiant");

        assertFalse(foundteacher.isEmpty(), "La liste des enseignants ne doit pas être vide.");
        assertEquals("professeur", foundteacher.get(0).getStatut(), "Le statut doi être professeur'.");
        assertFalse(foundstudent.isEmpty(), "la liste d'étudiant ne doit pas être vide");
        assertThat(foundstudent.get(0).getStatut()).isEqualTo("etudiant");
        assertEquals("etudiant", foundstudent.get(0).getStatut(), "Le statut doit être etudiant'.");

    }

    @Test
    public void whenFindAll_thenReturnPersonnes() {

        entityManager.persist(personne1);
        entityManager.persist(personne2);
        entityManager.flush();

        List<Personne> found = personneRepository.findAll();

        assertNotNull(found);
        assertFalse(found.isEmpty(), "la liste ne peut pas être vide");
        assertEquals(2, found.size());
    }

    @Test
    public void whenFindAllWithPageable_thenReturnPersonnesPage() {

        entityManager.persist(personne1);
        entityManager.persist(personne2);
        entityManager.flush();

        Page<Personne> found = personneRepository.findAll(PageRequest.of(0, 2));

        assertEquals(2, found.getContent().size(), "La taille doit être égal à 2.");

    }

    @Test
    public void whenPersistWithNullName_thenThrowException() {
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persist(personneerror);
            entityManager.flush();
        });
    }

    @Test
    public void whenPersistConstructeurwithnulldata_thenThrowException() {
        assertThrows(ValidationException.class, () -> {
            entityManager.persist(personnewithnotargument);
            entityManager.flush();
        });
    }

    @Test
    public void whenFindByIdWithNonExistingId_thenReturnNull() {
        UUID nonExistingId = UUID.randomUUID();
        Personne found = personneRepository.findById(nonExistingId).orElse(null);
        assertNull(found);
    }
}
