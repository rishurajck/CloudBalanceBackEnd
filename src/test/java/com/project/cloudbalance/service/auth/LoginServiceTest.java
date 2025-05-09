//package com.project.cloudbalance.service.auth;
//import com.project.cloudbalance.dto.login.LoginRequestDTO;
//import com.project.cloudbalance.dto.login.LoginResponseDTO;
//import com.project.cloudbalance.entity.Role;
//import com.project.cloudbalance.entity.User;
//import com.project.cloudbalance.exception.customException.InvalidCredentialsException;
//import com.project.cloudbalance.repository.UserRepository;
//import com.project.cloudbalance.security.AuthService;
//import com.project.cloudbalance.service.login.LoginService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import java.time.LocalDateTime;
//import java.util.Optional;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class LoginServiceTest {
//
//    @InjectMocks
//    private LoginService loginService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private AuthService authService;
//
//    private LoginRequestDTO loginRequestDTO;
//
//    private User user;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);  // Initialize mocks
//        loginRequestDTO = new LoginRequestDTO();
//        loginRequestDTO.setUsername("Rishu");
//        loginRequestDTO.setPassword("Rishu@1901");
//
//        user = new User();
//        user.setUsername("Rishu");
//        user.setFirstname("Rishu");
//        user.setRole(Role.valueOf("ADMIN"));
//        user.setLastLogin(LocalDateTime.now());
//    }
//
//    @Test
//    void testLogin_UserNotFound() {
//        when(userRepository.findByUsername(loginRequestDTO.getUsername())).thenReturn(Optional.empty());
//
//        UsernameNotFoundException thrown = assertThrows(
//                UsernameNotFoundException.class,
//                () -> loginService.login(loginRequestDTO)
//        );
//
//        assertEquals("User not found", thrown.getMessage());
//    }
//
//    @Test
//    void testLogin_EmptyUsernameOrPassword() {
//        loginRequestDTO.setUsername("");  // Empty username
//        loginRequestDTO.setPassword("password123");
//
//        InvalidCredentialsException thrown = assertThrows(
//                InvalidCredentialsException.class,
//                () -> loginService.login(loginRequestDTO)
//        );
//
//        assertEquals("Username or password cannot be null", thrown.getMessage());
//    }
//
//    @Test
//    void testLogin_InvalidCredentials() {
//        when(userRepository.findByUsername(loginRequestDTO.getUsername())).thenReturn(Optional.of(user));
//        when(authService.authenticateAndGenerateToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()))
//                .thenThrow(new InvalidCredentialsException("Invalid username or password"));
//
//        InvalidCredentialsException thrown = assertThrows(
//                InvalidCredentialsException.class,
//                () -> loginService.login(loginRequestDTO)
//        );
//
//        assertEquals("Invalid username or password", thrown.getMessage());
//    }
//
//    @Test
//    void testLogin_SuccessfulLogin() {
//        String generatedToken = "mockedToken";
//        when(userRepository.findByUsername(loginRequestDTO.getUsername())).thenReturn(Optional.of(user));
//        when(authService.authenticateAndGenerateToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()))
//                .thenReturn(generatedToken);
//
//        LoginResponseDTO response = loginService.login(loginRequestDTO);
//        LoginResponseDTO loginResponseDTO = (LoginResponseDTO) response.;
//
//        assertNotNull(loginResponseDTO);
//        assertEquals(generatedToken, loginResponseDTO.getToken());
//        assertEquals("Rishu", loginResponseDTO.getFirstname());
//        assertEquals("Rishu", loginResponseDTO.getUsername());
//        assertEquals("ADMIN", loginResponseDTO.getRole());
//
//        verify(userRepository, times(1)).save(any(User.class));
//    }
//}
