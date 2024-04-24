import org.example.controllers.PostController;
import org.example.dto.PostDTO;
import org.example.servicesImpl.MultipartServiceImpl;
import org.example.servicesImpl.PostServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {

    @Mock
    private PostServiceImpl postServiceMock;

    @InjectMocks
    private PostController postController;

    private MockMvc mockMvc;
    @Mock
    private MultipartServiceImpl multipartService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Resource resource;


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @Test
    public void testGetOne() throws Exception {
        PostDTO postDTO = new PostDTO(1,"", "Title 1", "This is a test post", new Date());
        when(postServiceMock.findOne(1)).thenReturn(postDTO);

        mockMvc.perform(get("/api/post/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Title 1")))
                .andExpect(jsonPath("$.information", is("This is a test post")));
    }

    @Test
    public void indexByDateDesc_ReturnsPostDTOList() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 1);

        Date newDate = calendar.getTime();

        List<PostDTO> mockPostDTOList = Arrays.asList(
                new PostDTO(2,"", "Title 2", "Content 2", newDate),
                new PostDTO(1,"", "Title 1", "Content 1", new Date())
        );

        when(postServiceMock.findAllByDateDesc()).thenReturn(mockPostDTOList);

        mockMvc.perform(get("/api/post"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Title 2")))
                .andExpect(jsonPath("$[1].title", is("Title 1")));
    }

    @Test
    public void getResourceFileTest() throws IOException {
        int postId = 1;
        PostDTO postDTO = new PostDTO();
        postDTO.setPostId(postId);
        postDTO.setPhoto("test.jpg");

        when(postServiceMock.findOne(postId)).thenReturn(postDTO);
        when(multipartService.loadFileAsResource(any(String.class))).thenReturn(resource);
        when(request.getServletContext()).thenReturn(new MockServletContext());
        when(resource.getFile()).thenReturn(new File("/src/test/java/config/test.jpg"));

        ResponseEntity<Resource> response = postController.getResourceFile(postId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("image/jpeg", response.getHeaders().getContentType().toString());
        assertEquals("attachment; filename=\"test.jpg\"", response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).get(0));

        // additional assertions if needed
    }


}


