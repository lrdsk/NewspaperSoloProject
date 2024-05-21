package org.example;

import org.example.dto.PostDTO;
import org.example.models.Post;
import org.example.repositories.PostRepository;
import org.example.services.servicesImpl.MultipartServiceImpl;
import org.example.services.servicesImpl.PostServiceImpl;
import org.example.util.mappers.PostMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceImplTests {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostMapper postMapper;
    @Mock
    private MultipartServiceImpl multipartService;

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    public void testFindAll() {
        List<Post> posts = new ArrayList<>();
        posts.add(new Post());
        posts.add(new Post());

        when(postRepository.findAll()).thenReturn(posts);
        when(postMapper.toDto(any(Post.class))).thenReturn(new PostDTO());

        List<PostDTO> postDTOs = postService.findAll();

        assertEquals(2, postDTOs.size());
    }

}

