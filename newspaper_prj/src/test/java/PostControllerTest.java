import config.TestConfig;
import org.example.config.SecurityConfig;
import org.example.controllers.PostController;
import org.example.dto.PostDTO;
import org.example.repositories.UserRepository;
import org.example.servicesImpl.PostServiceImpl;
import org.example.servicesImpl.UserDetailsServiceImpl;
import org.example.servicesImpl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = {SecurityConfig.class, UserDetailsServiceImpl.class})
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostServiceImpl postService;

    @Test
    void testIndexByDateDesc() throws Exception {
        List<PostDTO> posts = new ArrayList<>();
        posts.add(new PostDTO(1,"", "Title 1", "Content 1", new Date()));
        posts.add(new PostDTO(2,"", "Title 2", "Content 2", new Date()));
        when(postService.findAllByDateDesc()).thenReturn(
                posts
        );

        mockMvc.perform(get("/api/post"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Title 1")))
                .andExpect(jsonPath("$[1].title", is("Title 2")));
    }
}

