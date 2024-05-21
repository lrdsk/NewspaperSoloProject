package org.example;

import org.example.dto.UserDTO;
import org.example.models.User;
import org.example.repositories.UserRepository;
import org.example.services.servicesImpl.RegistrationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RegistrationServiceImplTests {

    private RegistrationServiceImpl registrationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        registrationService = new RegistrationServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    public void register_UserSavedSuccessfully() {
        UserDTO userDTO = new UserDTO("Test name", "Surname", "test@mail.com", "password123");
        User user = new User();
        user.setUserId(1);
        user.setName("Test name");
        user.setSurname("Surname");
        user.setEmail("test@mail.com");
        user.setPassword("password123");

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        registrationService.register(user);

        verify(passwordEncoder, times(1)).encode(user.getPassword());
        verify(userRepository, times(1)).save(user);
    }
}
