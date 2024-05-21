package org.example;

import org.example.controllers.UserController;
import org.example.security.JWTUtil;
import org.example.services.servicesImpl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserControllerTests {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private UserController userController;

    @Test
    public void testGetLikes() {
        String token = "Bearer your_token_here";

        String email = "test@example.com";

        when(jwtUtil.validateTokenAndRetrieveClaim(anyString())).thenReturn(email);
        when(userService.getSetPostLiked(email)).thenReturn(new HashSet<>(Arrays.asList(1, 2, 3)));

        Set<Integer> result = userController.getLikes(token);

        verify(jwtUtil).validateTokenAndRetrieveClaim(anyString());
        verify(userService).getSetPostLiked(email);

        assertNotNull(result);
        assertEquals(3, result.size());
    }

}

