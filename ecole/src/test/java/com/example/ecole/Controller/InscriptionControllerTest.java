package com.example.ecole.Controller;

import com.example.ecole.controller.InscriptionController;
import com.example.ecole.models.Inscription;
import com.example.ecole.models.Personne;
import com.example.ecole.repository.InscriptionRepository;
import com.example.ecole.repository.PersonneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InscriptionControllerTest {

    @InjectMocks
    private InscriptionController inscriptionController;

    @Mock
    private InscriptionRepository inscritRepository;

    @Mock
    private PersonneRepository personneRepository;

    @Mock
    private Pageable pageable;

    private Personne personne;
    private Inscription inscription;

    @BeforeEach
    public void setUp() {
        personne = new Personne();
        personne.setId(UUID.randomUUID());

        inscription = new Inscription();
        inscription.setId(UUID.randomUUID());
        inscription.setPersonne(personne);

    }

    @Test
    public void getAllInscriptionTest() {
        when(inscritRepository.findAll()).thenReturn(Arrays.asList(inscription));
        Authentication authentication = createAuthenticationWithAuthority("SCOPE_read:inscrit");
        ResponseEntity<List<Inscription>> responseEntity = inscriptionController.getAllInscription(pageable, authentication);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals(inscription, responseEntity.getBody().get(0));
    }

    @Test
    public void getPaginatedInscriptionsTest() {
        when(inscritRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Arrays.asList(inscription)));
        Authentication authentication = createAuthenticationWithAuthority("SCOPE_read:inscrit");
        ResponseEntity<Page<Inscription>> responseEntity = inscriptionController.getPaginatedInscriptions(pageable, authentication);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().getTotalElements());
        assertEquals(inscription, responseEntity.getBody().getContent().get(0));
    }

    @Test
    public void addInscritTest() {
        when(inscritRepository.save(any(Inscription.class))).thenReturn(inscription);
        when(personneRepository.findById(any(UUID.class))).thenReturn(Optional.of(personne));
        Authentication authentication = createAuthenticationWithAuthority("SCOPE_write:inscrit");
        ResponseEntity<Inscription> responseEntity = inscriptionController.addInscrit(inscription, UriComponentsBuilder.newInstance(), authentication);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(inscription, responseEntity.getBody());

        verify(inscritRepository, times(1)).save(any(Inscription.class));
    }

    @Test
    public void updateInscriptionTest() {
        when(inscritRepository.findById(any(UUID.class))).thenReturn(Optional.of(inscription));
        when(inscritRepository.save(any(Inscription.class))).thenReturn(inscription);
        Authentication authentication = createAuthenticationWithAuthority("SCOPE_write:inscrit");
        ResponseEntity<Inscription> responseEntity = inscriptionController.updateInscription(inscription.getId(), inscription, authentication);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(inscription, responseEntity.getBody());

        verify(inscritRepository, times(1)).save(any(Inscription.class));
    }

    @Test
    public void getAllInscriptionTestNoAuthority() {
        Authentication authentication = createAuthenticationWithAuthority("SCOPE_wrong:inscrit");
        ResponseEntity<List<Inscription>> responseEntity = inscriptionController.getAllInscription(pageable, authentication);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void addInscritTestNoPersonne() {
        when(personneRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        Authentication authentication = createAuthenticationWithAuthority("SCOPE_write:inscrit");
        ResponseEntity<Inscription> responseEntity = inscriptionController.addInscrit(inscription, UriComponentsBuilder.newInstance(), authentication);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void updateInscriptionTestNoInscription() {
        when(inscritRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        Authentication authentication = createAuthenticationWithAuthority("SCOPE_write:inscrit");
        ResponseEntity<Inscription> responseEntity = inscriptionController.updateInscription(inscription.getId(), inscription, authentication);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }


    private Authentication createAuthenticationWithAuthority(String authority) {
        Authentication authentication = mock(Authentication.class);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> authority);
        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);
        return authentication;
    }
}
