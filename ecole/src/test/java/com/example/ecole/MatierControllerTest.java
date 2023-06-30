package com.example.ecole;
import com.example.ecole.controller.MatiereController;
import com.example.ecole.models.Matiere;
import com.example.ecole.models.PaginatedMatiereResponse;
import com.example.ecole.repository.MatiereRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MatiereControllerTest {

    @Mock
    private MatiereRepository matiereRepository;

    @InjectMocks
    private MatiereController matiereController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMatieres() {
        List<Matiere> matieres = new ArrayList<>();
        when(matiereRepository.findAll()).thenReturn(matieres);
        ResponseEntity<List<Matiere>> response = matiereController.getAllMatieres(mock(Authentication.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(matieres, response.getBody());
        verify(matiereRepository, times(1)).findAll();
    }

    @Test
    void testAddMatiere() {
        Matiere matiere = new Matiere();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("/add/matieres");

        when(matiereRepository.save(matiere)).thenReturn(matiere);
        ResponseEntity<Matiere> response = matiereController.addMatiere(matiere, builder, mock(Authentication.class));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(matiere, response.getBody());
        verify(matiereRepository, times(1)).save(matiere);
    }

    @Test
    void testGetPaginatedMatieres() {
        // Création des données de test
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Matiere> matierePage = mock(Page.class);
        when(matiereRepository.findAll(pageRequest)).thenReturn(matierePage);
        when(matierePage.getContent()).thenReturn(new ArrayList<>());
        when(matierePage.getTotalPages()).thenReturn(1);
        when(matierePage.getTotalElements()).thenReturn(0L);
        ResponseEntity<PaginatedMatiereResponse> response = matiereController.getPaginatedMatieres(
                mock(Authentication.class), page, size);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalPages());
        assertEquals(0, response.getBody().getTotalElements());
        verify(matiereRepository, times(1)).findAll(pageRequest);
    }
}

