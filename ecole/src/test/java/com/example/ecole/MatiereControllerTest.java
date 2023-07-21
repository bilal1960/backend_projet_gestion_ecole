package com.example.ecole;
import com.example.ecole.controller.MatiereController;
import com.example.ecole.repository.MatiereRepository;
import com.example.ecole.models.Matiere;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.ArrayList;
import java.util.Collection;
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
        Authentication authentication = createAuthenticationWithAuthority("SCOPE_read:matiere");
        List<Matiere> matieres = new ArrayList<>();
        when(matiereRepository.findAll()).thenReturn(matieres);
        ResponseEntity<List<Matiere>> response = matiereController.getAllMatieres(authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(matieres, response.getBody());
        verify(matiereRepository, times(1)).findAll();
    }

    @Test
    void testAddMatiere() {
        Matiere matiere = new Matiere();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("/add/matieres");
        Authentication authentication = createAuthenticationWithAuthority("SCOPE_write:matiere");

        when(matiereRepository.save(matiere)).thenReturn(matiere);

        ResponseEntity<Matiere> response = matiereController.addMatiere(matiere, builder, authentication);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(matiere, response.getBody());
        verify(matiereRepository, times(1)).save(matiere);
    }
    @Test
    void testGetPaginatedMatieres() {
        Pageable pageable = mock(Pageable.class);
        Authentication authentication = createAuthenticationWithAuthority("SCOPE_read:inscrit");

        Page<Matiere> page = mock(Page.class);
        when(matiereRepository.findAll(any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<Matiere>> response = matiereController.getPaginatedMatieres(pageable, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());

        verify(matiereRepository, times(1)).findAll(any(Pageable.class));
    }

    private Authentication createAuthenticationWithAuthority(String authority) {
        Authentication authentication = mock(Authentication.class);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> authority);
        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);
        return authentication;
    }
}

