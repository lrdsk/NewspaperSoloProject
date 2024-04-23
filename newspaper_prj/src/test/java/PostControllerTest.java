import org.example.controllers.PostController;
import org.example.dto.PostDTO;
import org.example.servicesImpl.PostServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {

    @Mock
    private PostServiceImpl postServiceMock;

    @InjectMocks
    private PostController postController;

    @Test
    void indexByDateDesc_ReturnsPostDTOList() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 1);

        Date newDate = calendar.getTime();

        List<PostDTO> mockPostDTOList = Arrays.asList(
                new PostDTO(2,"", "Title 2", "Content 2", newDate),
                new PostDTO(1,"", "Title 1", "Content 1", new Date())
        );

        Mockito.when(postServiceMock.findAllByDateDesc()).thenReturn(mockPostDTOList);

        List<PostDTO> result = postController.indexByDateDesc();

        // Проверяем, что результат не null и содержит ожидаемые данные
        assertNotNull(result);
        assertEquals(2, result.size());

        // Проверяем, что данные соответствуют ожидаемым данным
        assertEquals("Title 2", result.get(0).getTitle());
        assertEquals("Content 2", result.get(0).getInformation());

        assertEquals("Title 1", result.get(1).getTitle());
        assertEquals("Content 1", result.get(1).getInformation());
    }
}


