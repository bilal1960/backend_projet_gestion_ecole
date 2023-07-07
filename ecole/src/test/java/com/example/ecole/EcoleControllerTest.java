package com.example.ecole;
import com.example.ecole.models.Ecole;
import com.example.ecole.repository.EcoleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import com.example.ecole.controller.EcoleController;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EcoleControllerTest {

    @InjectMocks
    private EcoleController ecoleController;

    @Mock
    private EcoleRepository ecoleRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private Logger logger;

    @Test
    void testGetAllEcoles() {
        MockitoAnnotations.openMocks(this);
        List<Ecole> ecoles = Collections.singletonList(new Ecole());
        when(ecoleRepository.findAll()).thenReturn(ecoles);
        ResponseEntity<List<Ecole>> responseEntity = ecoleController.getAllEcoles(authentication);
        assertEquals(ecoles, responseEntity.getBody());
        assertEquals(200, responseEntity.getStatusCodeValue());
        verify(logger).info("succès de l'affichage de la liste");
        verify(ecoleRepository, times(1)).findAll();
        verifyNoMoreInteractions(ecoleRepository);
    }
}

