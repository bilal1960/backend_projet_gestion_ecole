package com.example.ecole;
import com.example.ecole.controller.PersonneAjouterController;
import com.example.ecole.models.Personne;
import com.example.ecole.repository.PersonneRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
 import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.*;

class PersonneAjouterControllerTest {

    @InjectMocks
    private PersonneAjouterController personneAjouterController;

    @Mock
    private PersonneRepository personneRepository;

    @Mock
    private Authentication authentication;

    @Test
    void testGetAllPersonnes() {
        MockitoAnnotations.openMocks(this);

        List<Personne> personnes = Collections.singletonList(new Personne());

        when(personneRepository.findAll()).thenReturn(personnes);

        ResponseEntity<List<Personne>> responseEntity = personneAjouterController.getAllPersonnes(authentication);


    }
}

