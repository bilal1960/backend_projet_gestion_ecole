package com.example.ecole;
import com.example.ecole.models.Inscription;
import com.example.ecole.models.Matiere;
import com.example.ecole.models.Personne;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonneTest {

    private Personne personne;
    private UUID id;
    private String prenom;
    private String nom;
    private LocalDate naissance;
    private String nationalite;
    private String adresse;
    private String sexe;
    private List<Matiere> matieres;
    private List<Inscription> inscriptions;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        prenom = "John";
        nom = "Doe";
        naissance = LocalDate.of(2000, 1, 1);
        nationalite = "France";
        adresse = "123 Rue Test";
        sexe = "M";
        matieres = new ArrayList<>();
        inscriptions = new ArrayList<>();
        personne = new Personne(prenom, nom, naissance, nationalite, adresse, sexe, matieres);
        personne.setId(id);
        personne.setInscriptions(inscriptions);
    }

    @Test
    void testGetId() {
        assertEquals(id, personne.getId());
    }

    @Test
    void testGetPrenom() {
        assertEquals(prenom, personne.getPrenom());
    }

    @Test
    void testSetPrenom() {
        String newPrenom = "Jane";
        personne.setPrenom(newPrenom);
        assertEquals(newPrenom, personne.getPrenom());
    }

    @Test
    void testGetNom() {
        assertEquals(nom, personne.getNom());
    }

    @Test
    void testSetNom() {
        String newNom = "Smith";
        personne.setNom(newNom);
        assertEquals(newNom, personne.getNom());
    }

    @Test
    void testGetNaissance() {
        assertEquals(naissance, personne.getNaissance());
    }

    @Test
    void testSetNaissance() {
        LocalDate newNaissance = LocalDate.of(1990, 2, 15);
        personne.setNaissance(newNaissance);
        assertEquals(newNaissance, personne.getNaissance());
    }

    @Test
    void testGetNationalite() {
        assertEquals(nationalite, personne.getNationalite());
    }

    @Test
    void testSetNationalite() {
        String newNationalite = "USA";
        personne.setNationalite(newNationalite);
        assertEquals(newNationalite, personne.getNationalite());
    }

    @Test
    void testGetAdresse() {
        assertEquals(adresse, personne.getAdresse());
    }

    @Test
    void testSetAdresse() {
        String newAdresse = "456 Street";
        personne.setAdresse(newAdresse);
        assertEquals(newAdresse, personne.getAdresse());
    }

    @Test
    void testGetSexe() {
        assertEquals(sexe, personne.getSexe());
    }

    @Test
    void testSetSexe() {
        String newSexe = "F";
        personne.setSexe(newSexe);
        assertEquals(newSexe, personne.getSexe());
    }

    @Test
    void testGetMatieres() {
        assertEquals(matieres, personne.getMatieres());
    }

    @Test
    void testGetInscriptions() {
        assertEquals(inscriptions, personne.getInscriptions());
    }

    @Test
    void testSetInscriptions() {
        List<Inscription> newInscriptions = new ArrayList<>();
        personne.setInscriptions(newInscriptions);
        assertEquals(newInscriptions, personne.getInscriptions());
    }
}

