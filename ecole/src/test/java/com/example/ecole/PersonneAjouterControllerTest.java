package com.example.ecole;
import com.example.ecole.controller.PersonneAjouterController;
import com.example.ecole.models.Personne;
import com.example.ecole.repository.PersonneRepository;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PersonneAjouterControllerTest {
    @Mock
    private PersonneRepository personneRepository;

    @InjectMocks
    private PersonneAjouterController personneAjouterController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPersonnes() {
        Authentication authentication = createAuthenticationWithAuthority("SCOPE_read:personne");
        List<Personne> personnes = new ArrayList<>();
        when(personneRepository.findAll()).thenReturn(personnes);
        ResponseEntity<List<Personne>> response = personneAjouterController.getAllPersonnes(authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(personnes, response.getBody());
        verify(personneRepository, times(1)).findAll();
    }

    @Test
    void testGetPaginatedPersonnes() {
        Pageable pageable = mock(Pageable.class);
        Authentication authentication = createAuthenticationWithAuthority("SCOPE_read:personne");
        Page<Personne> page = mock(Page.class);
        when(personneRepository.findAll(any(Pageable.class))).thenReturn(page);
        ResponseEntity<Page<Personne>> response = personneAjouterController.getPaginatedInscriptions(pageable, authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
        verify(personneRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testAddPersonne() {
        Personne personne = new Personne();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("/add/personnes");
        Authentication authentication = createAuthenticationWithAuthority("SCOPE_write:personne");
        when(personneRepository.save(personne)).thenReturn(personne);
        ResponseEntity<Personne> response = personneAjouterController.addPersonne(personne, authentication, builder);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(personne, response.getBody());
        verify(personneRepository, times(1)).save(personne);
    }
    private Authentication createAuthenticationWithAuthority(String authority) {
        Authentication authentication = mock(Authentication.class);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> authority);
        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);
        return authentication;
    }
}

