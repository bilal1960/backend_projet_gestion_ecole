package com.example.ecole;
import com.example.ecole.models.Matiere;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.example.ecole.models.Personne;


class MatiereTest {

    private Matiere matiere;
    private UUID id;
    private String nom;
    private LocalDate debut;
    private LocalDate fin;
    private List<Personne> personnes;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        nom = "Mathématiques";
        debut = LocalDate.of(2023, 1, 1);
        fin = LocalDate.of(2023, 6, 30);
        personnes = new ArrayList<>();
        matiere = new Matiere(nom, debut, fin, personnes);
        matiere.setId(id);
    }

    @Test
    void testGetId() {
        assertEquals(id, matiere.getId());
    }

    @Test
    void testGetNom() {
        assertEquals(nom, matiere.getNom());
    }

    @Test
    void testSetNom() {
        String newNom = "Physique";
        matiere.setNom(newNom);
        assertEquals(newNom, matiere.getNom());
    }

    @Test
    void testGetDebut() {
        assertEquals(debut, matiere.getDebut());
    }

    @Test
    void testGetFin() {
        assertEquals(fin, matiere.getFin());
    }

    @Test
    void testGetPersonnes() {
        assertEquals(personnes, matiere.getPersonnes());
    }

    @Test
    void testSetId() {
        UUID newId = UUID.randomUUID();
        matiere.setId(newId);
        assertEquals(newId, matiere.getId());
    }

    @Test
    void testConstructor() {
        assertNotNull(matiere.getId());
        assertEquals(nom, matiere.getNom());
        assertEquals(debut, matiere.getDebut());
        assertEquals(fin, matiere.getFin());
        assertEquals(personnes, matiere.getPersonnes());
    }
}
