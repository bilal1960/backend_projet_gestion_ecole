package com.example.ecole.Model;

import com.example.ecole.models.Inscription;
import com.example.ecole.models.Personne;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class InscriptionTest {
    private Inscription inscription;
    private Personne personne;
    private LocalDate date = LocalDate.of(2010, 9, 10);
    private LocalDate date_inscrit = LocalDate.of(2023, 9, 10);


    @BeforeEach
    public void setUp() {

        personne = new Personne("maria", "Lolo", date, "Belge", "123 rue Test", "homme", "professeur",30.00f,"reginecamille@hotmail.com","a7555be7-6af7-4154-850d-ff71990dc924");
        inscription = new Inscription("Uccle", 500.00f, personne, date_inscrit,50.0f, "General", "4 secondaire");
    }

    @Test
    public void testInscriptionConstructor() {
        assertNotNull(inscription);
        assertEquals("Uccle", inscription.getCommune());
        assertEquals(500.0f, inscription.getMinerval());
        assertEquals(50.0f, inscription.getRembourser());
        assertEquals("General", inscription.getSection());
        assertEquals("4 secondaire", inscription.getSecondaire_anne());
        assertNotNull(inscription.getDate_inscrit());
        assertEquals(LocalDate.of(2023, 9, 10), inscription.getDate_inscrit());
        assertNotNull(inscription.getPersonne());
    }
}
