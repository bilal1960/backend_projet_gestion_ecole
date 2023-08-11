package com.example.ecole.Model;

import com.example.ecole.models.Ecole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EcoleTest {

    private Ecole ecole;


    @BeforeEach
    void setUp() {
        ecole = new Ecole("Ecole Royale", "123 Rue de l'éducation", "ecoleRoyale@hotmail.com", "0123456789", "secondaire");

    }

    @Test
    void TestConstructeur() {

        assertNotNull(ecole);
        assertEquals("Ecole Royale", ecole.getNom());
        assertEquals("123 Rue de l'éducation", ecole.getAdresse());
        assertEquals("ecoleRoyale@hotmail.com", ecole.getMail());
        assertEquals("0123456789", ecole.getNumber());
        assertEquals("secondaire", ecole.getType());

    }

}

