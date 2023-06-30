package com.example.ecole;
import com.example.ecole.models.Inscription;
import com.example.ecole.repository.InscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.util.UriComponentsBuilder;
import  com.example.ecole.controller.InscriptionController;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class InscriptionControllerTest {

    @Mock
    private InscriptionRepository inscriptionRepository;

    @InjectMocks
    private InscriptionController inscriptionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllInscription() {

        List<Inscription> inscriptions = new ArrayList<>();
        when(inscriptionRepository.findAll()).thenReturn(inscriptions);
        ResponseEntity<List<Inscription>> response = inscriptionController.getAllInscription(mock(Pageable.class), mock(Authentication.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inscriptions, response.getBody());

        verify(inscriptionRepository, times(1)).findAll();
    }

    @Test
    void testGetPaginatedInscriptions() {
        Page<Inscription> page = mock(Page.class);
        when(inscriptionRepository.findAll(any(Pageable.class))).thenReturn(page);
        ResponseEntity<Page<Inscription>> response = inscriptionController.getPaginatedInscriptions(mock(Pageable.class), mock(Authentication.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
        verify(inscriptionRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testAddInscrit() {
        Inscription inscription = new Inscription();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("/add/inscriptions");
        when(inscriptionRepository.save(inscription)).thenReturn(inscription);
        ResponseEntity<Inscription> response = inscriptionController.addInscrit(inscription, builder, mock(Authentication.class));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(inscription, response.getBody());
        verify(inscriptionRepository, times(1)).save(inscription);
    }
}

