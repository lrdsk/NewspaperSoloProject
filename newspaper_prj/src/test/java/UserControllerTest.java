import org.example.controllers.UserController;
import org.example.servicesImpl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void testGetLikes() throws Exception {
        mockMvc.perform(get("/api/users/likes")
                        .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVc2VyIGRldGFpbHMiLCJpc3MiOiJtaWtoYWlsb3YiLCJleHAiOjE3MTM4NjQ3ODksImlhdCI6MTcxMzg2MTE4OSwiZW1haWwiOiJvQG1haWwucnUifQ.Jy0dAL8m4hw13SmgUs01UjLvEGJnC7YwsLHOCkzLQB4"))
                .andExpect(status().isOk());

        // Устанавливаем ожидаемый результат для mock сервиса userService
        Set<Integer> expectedLikes = new HashSet<>(Arrays.asList(1, 2, 3));
        when(userService.getSetPostLiked("test@example.com")).thenReturn(expectedLikes);

        // Вызываем метод контроллера
        Set<Integer> actualLikes = userController.getLikes();

        // Проверяем, что результат совпадает с ожидаемым
        assertEquals(expectedLikes, actualLikes);
    }
}
