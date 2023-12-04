package com.example.ecole.Model;

import com.example.ecole.models.Personne;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;


public class PersonneTest {

    private Personne personne;
    private LocalDate birthDate = LocalDate.now().minusYears(21);

    @BeforeEach
    public void setUp() {
        personne = new Personne("lolo", "laura", birthDate, "Belge", "123 rue Test", "homme", "professeur",30.00f,"reginecamille@hotmail.com","a7555be7-6af7-4154-850d-ff71990dc924");

    }

    @Test
    public void TestConstructeur() {
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
