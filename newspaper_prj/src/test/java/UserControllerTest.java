import org.example.controllers.UserController;
import org.example.security.JWTUtil;
import org.example.servicesImpl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserServiceImpl userService;

    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private UserController userController;

    @Test
    public void testGetLikes() {
        String fakeToken = "Bearer abc123";
        String fakeEmail = "test@example.com";

        when(jwtUtil.validateTokenAndRetrieveClaim(anyString())).thenReturn(fakeEmail);

        Set<Integer> expectedLikes = new HashSet<>();
        expectedLikes.add(1);
        expectedLikes.add(2);

        when(userService.getSetPostLiked(eq(fakeEmail))).thenReturn(expectedLikes);

        Set<Integer> result = userController.getLikes(fakeToken);

        verify(userService).getSetPostLiked(eq(fakeEmail));

        assertEquals(expectedLikes, result);
    }
}
