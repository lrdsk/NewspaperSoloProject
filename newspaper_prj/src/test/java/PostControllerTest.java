
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controllers.PostController;
import org.example.dto.PostDTO;
import org.example.models.User;
import org.example.security.CustomUserDetails;
import org.example.servicesImpl.LikeServiceImpl;
import org.example.servicesImpl.MultipartServiceImpl;
import org.example.servicesImpl.PostServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    private LikeServiceImpl likeService;

    private static Path path = Paths.get("src/main/resources/template/test.jpg");

    @BeforeAll
    public static void configure() throws IOException {
        if(!Files.exists(path)) {
            File file = new File(path.toAbsolutePath().toString());
            boolean b = file.createNewFile();
        }
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @Test
    public void testGetOne_ReturnsValidPersonDTO() throws Exception {
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
        MultipartServiceImpl multipartServiceCurrent = new MultipartServiceImpl();

        int postId = 1;
        PostDTO postDTO = new PostDTO();
        postDTO.setPostId(postId);
        postDTO.setPhoto("test.jpg");
        
        Resource response = multipartServiceCurrent.loadFileAsResource(postDTO.getPhoto());

        assertEquals("test.jpg", response.getFilename());
    }

    @Test
    public void testSetLikeToPost() {
        int postId = 123;
        String fakeUsername = "test@example.com";
        boolean expectedIsLiked = true;

        Authentication fakeAuthentication = mock(Authentication.class);
        User user = new User();
        user.setEmail(fakeUsername);
        user.setPassword("password");
        user.setRole("ROLE_USER");
        CustomUserDetails fakeUserDetails = new CustomUserDetails(user);
        when(fakeAuthentication.getPrincipal()).thenReturn(fakeUserDetails);
        SecurityContextHolder.getContext().setAuthentication(fakeAuthentication);

        when(likeService.setLikeToPost(eq(fakeUsername), eq(postId))).thenReturn(expectedIsLiked);

        HttpEntity<Boolean> response = postController.setLikeToPost(postId);

        verify(likeService).setLikeToPost(eq(fakeUsername), eq(postId));

        assertEquals(expectedIsLiked, response.getBody());
    }

    @Test
    public void testGetCountLikes() {
        int postId = 1;
        int expectedCount = 5;
        when(likeService.getCountLikes(postId)).thenReturn(expectedCount);

        int result = postController.getCountLikes(postId);

        assertEquals(expectedCount, result);
    }

    @Test
    public void testAddPost() throws Exception {
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("Test Title");
        postDTO.setInformation("Test Information");

        MockMultipartFile file = new MockMultipartFile("photoFile", "test.jpg", "image/jpeg", "some image".getBytes());

        given(multipartService.savePhoto(file)).willReturn("test.jpg");

        ObjectMapper objectMapper = new ObjectMapper();
        String postJson = objectMapper.writeValueAsString(postDTO);

        mockMvc.perform(multipart("/api/post")
                        .file(file)
                        .param("postDTO", postJson)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string("Post saved"));
    }

    @AfterAll
    public static void afterClass() throws IOException {
        if(Files.exists(path)){
            Files.delete(path);
        }
    }


}


