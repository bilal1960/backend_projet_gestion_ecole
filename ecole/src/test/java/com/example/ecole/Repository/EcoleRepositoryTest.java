package com.example.ecole.Repository;

import com.example.ecole.models.Ecole;
import com.example.ecole.repository.EcoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.UUID;

@DataJpaTest
public class EcoleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EcoleRepository ecoleRepository;

    Ecole ecole1;

    UUID nonExistingId = UUID.randomUUID();


    @BeforeEach
    public void setUp() {
        ecole1 = new Ecole("ecole Royale", "rue du parc royal", "ecoleRoyale@hotmail.com", "0487000789", "secondaire");
    }

    @Test
    public void whenFindById_thenReturnEcole() {

        entityManager.persist(ecole1);
        entityManager.flush();

        Ecole found = ecoleRepository.findEcoleById(ecole1.getId());

        assertNotNull(found);
        assertEquals(ecole1.getNom(), found.getNom());
        assertEquals(ecole1.getAdresse(), found.getAdresse());
        assertEquals(ecole1.getMail(), found.getMail());
        assertEquals(ecole1.getNumber(), found.getNumber());
        assertEquals(ecole1.getType(), found.getType());

    }

    @Test
    public void whenFindByIdWithNonExistingId_thenReturnNull() {

        Ecole found = ecoleRepository.findEcoleById(nonExistingId);
        assertNull(found);
    }
}

