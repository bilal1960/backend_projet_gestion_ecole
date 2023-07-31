package com.example.ecole.Respository;
import com.example.ecole.models.Inscription;
import com.example.ecole.models.Matiere;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    public  void setUp(){
        List<Matiere> matieres = new ArrayList<>();
        List<Inscription> inscriptions = new ArrayList<>();

        personne1 = new Personne("lolo", "laura", birthDate, "Belge", "123 rue Test", "homme", "professeur", matieres, inscriptions);
        personne2 = new Personne("laura", "lulu", birthDateinscrit, "Belge", "123 rue Test2", "homme", "etudiant", matieres, inscriptions);
        personneerror = new Personne(null, "laura",birthDate, "Belge", "123 rue Test", "homme", "professeur", matieres, inscriptions);
        personnewithnotargument = new Personne();
    }

    @Test
    public void whenFindById_thenReturnPersonne() {


        entityManager.persist(personne1);
        entityManager.flush();

        Personne found = personneRepository.findById(personne1.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getNom()).isEqualTo(personne1.getNom());
    }

    @Test
    public void whenFindAllByStatut_thenReturnPersonnes() {

        entityManager.persist(personne1);

        entityManager.persist(personne2);
        entityManager.flush();

        List<Personne> foundteacher = personneRepository.findAllByStatut("professeur");
        List<Personne> foundstudent = personneRepository.findAllByStatut("etudiant");

        assertThat(foundteacher).isNotEmpty();
        assertThat(foundteacher.get(0).getStatut()).isEqualTo("professeur");
        assertThat(foundstudent).isNotEmpty();
        assertThat(foundstudent.get(0).getStatut()).isEqualTo("etudiant");
    }

    @Test
    public void whenFindAll_thenReturnPersonnes() {

        entityManager.persist(personne1);

        entityManager.persist(personne2);
        entityManager.flush();


        List<Personne> found = personneRepository.findAll();

        assertThat(found).isNotEmpty();
        assertThat(found).hasSize(2);
    }

    @Test
    public void whenFindAllWithPageable_thenReturnPersonnesPage() {

        entityManager.persist(personne1);

        entityManager.persist(personne2);
        entityManager.flush();

        Page<Personne> found = personneRepository.findAll(PageRequest.of(0, 2));

        assertThat(found.getContent()).hasSize(2);
    }

    @Test
    public void whenPersistWithNullName_thenThrowException() {
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persist(personneerror);
            entityManager.flush();
        });
    }
    @Test
    public  void whenPersistConstructeurwithnulldata_thenThrowException() {
        assertThrows(ValidationException.class, () -> {
            entityManager.persist(personnewithnotargument);
            entityManager.flush();
        });
    }

    @Test
    public void whenFindByIdWithNonExistingId_thenReturnNull() {
        UUID nonExistingId = UUID.randomUUID();
        Personne found = personneRepository.findById(nonExistingId).orElse(null);
        assertThat(found).isNull();
    }
}
