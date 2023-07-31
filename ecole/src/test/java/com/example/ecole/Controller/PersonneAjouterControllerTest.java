package com.example.ecole.Controller;

import com.example.ecole.controller.PersonneAjouterController;
import com.example.ecole.models.Personne;
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
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonneAjouterControllerTest {

    @InjectMocks
    private PersonneAjouterController personneAjouterController;

    @Mock
    private PersonneRepository personneRepository;

    private Personne personne;

    @BeforeEach
    public void setUp() {
        personne = new Personne();
        personne.setId(UUID.randomUUID());
        personne.setStatut("etudiant");
    }

    private Authentication createAuthenticationWithAuthority(String authority) {
        Authentication authentication = mock(Authentication.class);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> authority);
        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);
        return authentication;
    }

    @Test
    public void getAllPersonnesTest() {
        when(personneRepository.findAllByStatut("etudiant")).thenReturn(Arrays.asList(personne));

        Authentication authentication = createAuthenticationWithAuthority("SCOPE_read:personne");
        ResponseEntity<List<Personne>> responseEntity = personneAjouterController.getAllPersonnes(authentication);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Arrays.asList(personne), responseEntity.getBody());
    }

    @Test
    public void getAllPersonnesNoAuthorityTest() {
        Authentication authentication = createAuthenticationWithAuthority("SCOPE_wrong:personne");
        ResponseEntity<List<Personne>> responseEntity = personneAjouterController.getAllPersonnes(authentication);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void getAllProfesseursTest() {
        Personne professeur = new Personne();
        professeur.setId(UUID.randomUUID());
        professeur.setStatut("professeur");

        when(personneRepository.findAllByStatut("professeur")).thenReturn(Arrays.asList(professeur));

        Authentication authentication = createAuthenticationWithAuthority("SCOPE_read:personne");
        ResponseEntity<List<Personne>> responseEntity = personneAjouterController.getAllProfesseurs(authentication);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Arrays.asList(professeur), responseEntity.getBody());
    }

    @Test
    public void getAllProfesseursNoAuthorityTest() {
        Authentication authentication = createAuthenticationWithAuthority("SCOPE_wrong:personne");
        ResponseEntity<List<Personne>> responseEntity = personneAjouterController.getAllProfesseurs(authentication);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void getPaginatedInscriptionsTest() {
        List<Personne> personnes = Arrays.asList(personne);
        Page<Personne> personnePage = new PageImpl<>(personnes);

        when(personneRepository.findAll(any(Pageable.class))).thenReturn(personnePage);

        Authentication authentication = createAuthenticationWithAuthority("SCOPE_read:personne");
        ResponseEntity<Page<Personne>> responseEntity = personneAjouterController.getPaginatedInscriptions(Pageable.unpaged(), authentication);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(personnePage, responseEntity.getBody());
    }

    @Test
    public void getPaginatedInscriptionsNoAuthorityTest() {
        Authentication authentication = createAuthenticationWithAuthority("SCOPE_wrong:personne");
        ResponseEntity<Page<Personne>> responseEntity = personneAjouterController.getPaginatedInscriptions(Pageable.unpaged(), authentication);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void addPersonneTest() {
        UUID personneId = UUID.randomUUID();
        personne.setId(personneId);

        // Set naissance so the age is 21 or more
        LocalDate birthDate = LocalDate.now().minusYears(21);
        personne.setNaissance(birthDate);

        when(personneRepository.save(any(Personne.class))).thenReturn(personne);

        Authentication authentication = createAuthenticationWithAuthority("SCOPE_write:personne");
        ResponseEntity<Personne> responseEntity = personneAjouterController.addPersonne(personne, authentication, UriComponentsBuilder.newInstance());

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(personne, responseEntity.getBody());
    }


    @Test
    public void addPersonneUnderageProfesseurTest() {
        Personne personne = new Personne();
        personne.setId(UUID.randomUUID());
        personne.setStatut("professeur");

        // Simulating an underage professeur (20 years old).
        LocalDate dateOfBirth = LocalDate.now().minusYears(20);
        personne.setNaissance(dateOfBirth);

        Authentication authentication = createAuthenticationWithAuthority("SCOPE_write:personne");
        ResponseEntity<Personne> responseEntity = personneAjouterController.addPersonne(personne, authentication, UriComponentsBuilder.newInstance());

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void addPersonneNoAuthorityTest() {
        Authentication authentication = createAuthenticationWithAuthority("SCOPE_wrong:personne");
        ResponseEntity<Personne> responseEntity = personneAjouterController.addPersonne(personne, authentication, UriComponentsBuilder.newInstance());

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void updatePersonneTest() {
        UUID personneId = UUID.randomUUID();
        personne.setId(personneId);
        Personne updatedPersonne = new Personne();
        updatedPersonne.setAdresse("New Address");

        when(personneRepository.findById(personneId)).thenReturn(Optional.of(personne));
        when(personneRepository.save(any(Personne.class))).thenReturn(updatedPersonne);

        Authentication authentication = createAuthenticationWithAuthority("SCOPE_write:personne");
        ResponseEntity<Personne> responseEntity = personneAjouterController.updatePersonne(personneId, updatedPersonne, authentication);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedPersonne.getAdresse(), responseEntity.getBody().getAdresse());
    }

    @Test
    public void updatePersonneNotFoundTest() {
        UUID personneId = UUID.randomUUID();

        when(personneRepository.findById(personneId)).thenReturn(Optional.empty());

        Authentication authentication = createAuthenticationWithAuthority("SCOPE_write:personne");
        ResponseEntity<Personne> responseEntity = personneAjouterController.updatePersonne(personneId, personne, authentication);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void updatePersonneNoAuthorityTest() {
        UUID personneId = UUID.randomUUID();

        Authentication authentication = createAuthenticationWithAuthority("SCOPE_wrong:personne");
        ResponseEntity<Personne> responseEntity = personneAjouterController.updatePersonne(personneId, personne, authentication);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }
}


