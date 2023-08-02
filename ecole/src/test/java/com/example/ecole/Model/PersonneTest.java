package com.example.ecole.Model;

import com.example.ecole.models.Personne;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PersonneTest {

    private Personne personne;
    private  LocalDate birthDate = LocalDate.now().minusYears(21);

    @BeforeEach
    public void setUp() {
        personne = new Personne("lolo", "laura", birthDate, "Belge", "123 rue Test", "homme", "professeur");

    }
      @Test
    public  void TestConstructeur(){
        assertNotNull(personne);
        assertEquals("lolo", personne.getPrenom());
        assertEquals("laura", personne.getNom());
        assertEquals(birthDate, personne.getNaissance());
        assertEquals("Belge", personne.getNationalite());
        assertEquals("123 rue Test", personne.getAdresse());
        assertEquals("homme", personne.getSexe());
        assertEquals("professeur", personne.getStatut());

      }



}
