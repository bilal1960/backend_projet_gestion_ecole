package com.example.ecole.Model;

import com.example.ecole.models.Matiere;
import com.example.ecole.models.Personne;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class MatiereTest {

    Matiere matiere;

    LocalDate datedbut = LocalDate.of(2023, 9, 10);
    LocalDate datefin = LocalDate.of(2024, 1, 10);
    Personne professeur;
    LocalDate birthDate = LocalDate.now().minusYears(21);
    @JsonFormat(pattern = "HH:mm")
    LocalTime debutime = LocalTime.of(9,0);
    @JsonFormat(pattern = "HH:mm")
    LocalTime fintime = LocalTime.of(16,30);




    @BeforeEach
    void setUp(){

        professeur = new Personne("lolo", "laura", birthDate, "Belge", "123 rue Test", "homme", "professeur");
        matiere = new Matiere("Mathématiques",datedbut,datefin, professeur, debutime, fintime,"A400", "lundi", "3 secondaire");
    }
    @Test
    public void testMatiereConstructor() {
        assertNotNull(matiere);
        assertEquals("Mathématiques", matiere.getNom());
        assertEquals(datedbut, matiere.getDebut());
        assertEquals(datefin, matiere.getFin());
        assertEquals(professeur, matiere.getPersonne());
        assertEquals(debutime, matiere.getDebutime());
        assertEquals(fintime, matiere.getFintime());
        assertEquals("A400", matiere.getLocal());
        assertEquals("lundi", matiere.getJour());
        assertEquals("3 secondaire", matiere.getSecondaire());
    }
}
