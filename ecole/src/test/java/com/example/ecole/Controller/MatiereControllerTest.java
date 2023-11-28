package com.example.ecole.Controller;

import com.example.ecole.controller.MatiereController;
import com.example.ecole.models.Matiere;
import com.example.ecole.models.Personne;
import com.example.ecole.repository.MatiereRepository;
import com.example.ecole.repository.PersonneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.util.UriComponentsBuilder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)

public class MatiereControllerTest {

    @InjectMocks
    private MatiereController matiereController;

    @Mock
    private MatiereRepository matiereRepository;

    @Mock
    private PersonneRepository personneRepository;

    private Personne personne;
    private Matiere matiere;

    @Mock
    private Pageable pageable;


    @BeforeEach
    public void setUp() {
        personne = new Personne();
        personne.setId(UUID.randomUUID());

        matiere = new Matiere();
        matiere.setId(UUID.randomUUID());
        matiere.setDebut(LocalDate.of(2023,9,28));
        matiere.setFin(LocalDate.of(2023,1,15));
        matiere.setDebutime(LocalTime.of(10, 0));
        matiere.setFintime(LocalTime.of(12, 0));
        matiere.setJour("lundi");
        matiere.setLocal("A101");
        matiere.setSecondaire("2 secondaire");
        matiere.setPersonne(personne);
    }

    @Test
    public void getAllPagineMatiereTest() {
        List<Matiere> matieres = Arrays.asList(new Matiere(), new Matiere());
        Page<Matiere> matieresPage = new PageImpl<>(matieres);
        Authentication authentication = createAuthenticationWithAuthority("SCOPE_read:matiere");

        when(matiereRepository.findAll(any(Pageable.class))).thenReturn(matieresPage);

        ResponseEntity<Page<Matiere>> responseEntity = matiereController.getPaginatedMatieres(pageable, authentication);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(matieresPage, responseEntity.getBody());

        verify(matiereRepository, times(1)).findAll(pageable);
    }



    @Test
    public void addMatiereTestNoPersonne() {
        when(personneRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        Authentication authentication = createAuthenticationWithAuthority("SCOPE_write:matiere");
        ResponseEntity<?> responseEntity = matiereController.addMatiere(matiere, UriComponentsBuilder.newInstance(), authentication);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void addMatiereTestNoAuthority() {
        when(personneRepository.findById(any(UUID.class))).thenReturn(Optional.of(personne));

        Authentication authentication = createAuthenticationWithAuthority("SCOPE_wrong:matiere");
        ResponseEntity<?> responseEntity = matiereController.addMatiere(matiere, UriComponentsBuilder.newInstance(), authentication);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        verify(matiereRepository, never()).save(any(Matiere.class));
    }

    @Test
    public void addMatiereTestInvalidTime() {
        matiere.setDebutime(LocalTime.of(14, 0));
        matiere.setFintime(LocalTime.of(12, 0));

        Authentication authentication = createAuthenticationWithAuthority("SCOPE_write:matiere");
        ResponseEntity<?> responseEntity = matiereController.addMatiere(matiere, UriComponentsBuilder.newInstance(), authentication);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(matiereRepository, never()).save(any(Matiere.class));
    }


    private Authentication createAuthenticationWithAuthority(String authority) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(() -> authority);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);
        return authentication;
    }



    @Test
    public void updateMatiereNotMatiere() {
        when(matiereRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        Authentication authentication = createAuthenticationWithAuthority("SCOPE_write:matiere");
        ResponseEntity<?> responseEntity = matiereController.updateMatiere(matiere.getId(), matiere, authentication);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}
