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
class InscriptionTest {

    private Inscription inscription;
    private UUID id;
    private String nom;
    private String prenom;
    private LocalDate naissance;
    private String nationalite;
    private String sexe;
    private String adresse;
    private String commune;
    private float minerval;
    private Personne personne;
    private List<Matiere> matieres;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        nom = "Doe";
        prenom = "John";
        naissance = LocalDate.of(2000, 1, 1);
        nationalite = "France";
        sexe = "M";
        adresse = "123 Rue Test";
        commune = "Paris";
        minerval = 100.0f;
        personne = new Personne();
        matieres = new ArrayList<>();
        inscription = new Inscription(id, nom, prenom, naissance, nationalite, sexe, adresse, commune,
                minerval, personne, matieres);
    }

    @Test
    void testGetId() {
        assertEquals(id, inscription.getId());
    }

    @Test
    void testSetId() {
        UUID newId = UUID.randomUUID();
        inscription.setId(newId);
        assertEquals(newId, inscription.getId());
    }

    @Test
    void testGetNom() {
        assertEquals(nom, inscription.getNom());
    }

    @Test
    void testSetNom() {
        String newNom = "Smith";
        inscription.setNom(newNom);
        assertEquals(newNom, inscription.getNom());
    }

    @Test
    void testGetPrenom() {
        assertEquals(prenom, inscription.getPrenom());
    }

    @Test
    void testSetPrenom() {
        String newPrenom = "Jane";
        inscription.setPrenom(newPrenom);
        assertEquals(newPrenom, inscription.getPrenom());
    }

}