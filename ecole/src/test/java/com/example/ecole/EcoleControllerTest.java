package com.example.ecole;
import com.example.ecole.controller.EcoleController;
import com.example.ecole.models.Ecole;
import com.example.ecole.repository.EcoleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @Test
    void testGetAllEcoles() {
        MockitoAnnotations.openMocks(this);
        List<Ecole> ecoles = Collections.singletonList(new Ecole());
        when(ecoleRepository.findAll()).thenReturn(ecoles);
        ResponseEntity<List<Ecole>> responseEntity = ecoleController.getAllEcoles(authentication);
        assertEquals(ecoles, responseEntity.getBody());
        assertEquals(200, responseEntity.getStatusCodeValue());
        verify(ecoleRepository, times(1)).findAll();
        verifyNoMoreInteractions(ecoleRepository);
    }
}

