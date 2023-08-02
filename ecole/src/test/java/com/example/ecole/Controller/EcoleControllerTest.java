package com.example.ecole.Controller;

import com.example.ecole.controller.EcoleController;
import com.example.ecole.models.Ecole;
import com.example.ecole.repository.EcoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EcoleControllerTest {

    @InjectMocks
    private EcoleController ecoleController;

    @Mock
    private EcoleRepository ecoleRepository;

    private Ecole ecole;

    @BeforeEach
    public void setUp() {

        ecole = new Ecole();
        ecole.setId(UUID.randomUUID());
    }

    @Test
    public void getEcoleTest() {
        when(ecoleRepository.findEcoleById(any(UUID.class))).thenReturn(ecole);
        ResponseEntity<Ecole> responseEntity = ecoleController.getEcole();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(ecole, responseEntity.getBody());
    }

    @Test
    public void getEcoleNotFoundTest() {
        when(ecoleRepository.findEcoleById(any(UUID.class))).thenReturn(null);
        ResponseEntity<Ecole> responseEntity = ecoleController.getEcole();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void updateEcoleTest() {
        Ecole updatedEcole = new Ecole();
        updatedEcole.setId(UUID.randomUUID());
        updatedEcole.setAdresse("new address");
        updatedEcole.setMail("new email");
        updatedEcole.setNumber("new number");
        updatedEcole.setType("new type");

        when(ecoleRepository.findEcoleById(any(UUID.class))).thenReturn(ecole);
        when(ecoleRepository.save(any(Ecole.class))).thenReturn(updatedEcole);

        Authentication authentication = createAuthenticationWithAuthority("SCOPE_write:ecole");
        ResponseEntity<Ecole> responseEntity = ecoleController.updateEcole(updatedEcole.getId(), updatedEcole, authentication);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedEcole, responseEntity.getBody());

        verify(ecoleRepository, times(1)).save(any(Ecole.class));
    }

    @Test
    public void updateEcoleNoAuthorityTest() {
        Authentication authentication = createAuthenticationWithAuthority("SCOPE_wrong:ecole");
        ResponseEntity<Ecole> responseEntity = ecoleController.updateEcole(ecole.getId(), ecole, authentication);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    private Authentication createAuthenticationWithAuthority(String authority) {
        Authentication authentication = mock(Authentication.class);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> authority);
        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);
        return authentication;
    }
}

