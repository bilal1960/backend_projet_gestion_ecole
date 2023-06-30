package com.example.ecole;
import com.example.ecole.models.Ecole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EcoleTest {

    private Ecole ecole;
    private UUID id;
    private String nom;
    private String adresse;
    private String type;
    private String mail;
    private String number;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        nom = "Ecole ABC";
        adresse = "123 Rue Test";
        type = "Primaire";
        mail = "ecole@abc.com";
        number = "123456789";
        ecole = new Ecole(nom, adresse, mail, number, type);
        ecole.setId(id);
    }

    @Test
    void testGetId() {
        assertEquals(id, ecole.getId());
    }

    @Test
    void testGetNom() {
        assertEquals(nom, ecole.getNom());
    }

    @Test
    void testSetNom() {
        String newNom = "Ecole XYZ";
        ecole.setNom(newNom);
        assertEquals(newNom, ecole.getNom());
    }

    @Test
    void testGetAdresse() {
        assertEquals(adresse, ecole.getAdresse());
    }

    @Test
    void testSetAdresse() {
        String newAdresse = "456 Street";
        ecole.setAdresse(newAdresse);
        assertEquals(newAdresse, ecole.getAdresse());
    }

    @Test
    void testGetType() {
        assertEquals(type, ecole.getType());
    }

    @Test
    void testSetType() {
        String newType = "Secondaire";
        ecole.setType(newType);
        assertEquals(newType, ecole.getType());
    }

    @Test
    void testGetMail() {
        assertEquals(mail, ecole.getMail());
    }

    @Test
    void testSetMail() {
        String newMail = "ecole@xyz.com";
        ecole.setMail(newMail);
        assertEquals(newMail, ecole.getMail());
    }

    @Test
    void testGetNumber() {
        assertEquals(number, ecole.getNumber());
    }

    @Test
    void testSetNumber() {
        String newNumber = "987654321";
        ecole.setNumber(newNumber);
        assertEquals(newNumber, ecole.getNumber());
    }
}

