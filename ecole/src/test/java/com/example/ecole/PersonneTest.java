package com.example.ecole;
import com.example.ecole.models.Personne;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class PersonneTest {

    @Test
    public void testGettersAndSetters() {
        // Create a sample UUID
        UUID id = UUID.randomUUID();

        // Create a sample Personne object
        Personne personne = new Personne(id, "John", "Doe", LocalDate.of(1990, 1, 1),
                "France", "123 Main Street", "Male");

        // Test the getters
        assertEquals(id, personne.getId());
        assertEquals("John", personne.getPrenom());
        assertEquals("Doe", personne.getNom());
        assertEquals(LocalDate.of(1990, 1, 1), personne.getNaissance());
        assertEquals("France", personne.getNationalite());
        assertEquals("123 Main Street", personne.getAdresse());
        assertEquals("Male", personne.getSexe());

        // Test the setters
        UUID newId = UUID.randomUUID();
        personne.setId(newId);
        assertEquals(newId, personne.getId());

        personne.setPrenom("Jane");
        assertEquals("Jane", personne.getPrenom());

        personne.setNom("Smith");
        assertEquals("Smith", personne.getNom());

        LocalDate newNaissance = LocalDate.of(1995, 2, 15);
        personne.setNaissance(newNaissance);
        assertEquals(newNaissance, personne.getNaissance());

        personne.setNationalite("USA");
        assertEquals("USA", personne.getNationalite());

        personne.setAdresse("456 Elm Street");
        assertEquals("456 Elm Street", personne.getAdresse());

        personne.setSexe("Female");
        assertEquals("Female", personne.getSexe());
    }

    @Test
    public void testToString() {
        UUID id = UUID.randomUUID();
        Personne personne = new Personne(id, "John", "Doe", LocalDate.of(1990, 1, 1),
                "France", "123 Main Street", "Male");

        String expectedString = "Personne{" +
                "id=" + id +
                ", prenom='John'" +
                ", nom='Doe'" +
                ", naissance=" + LocalDate.of(1990, 1, 1) +
                ", nationalite='France'" +
                ", adresse='123 Main Street'" +
                ", sexe='Male'" +
                '}';

        assertEquals(expectedString, personne.toString());
    }
}

