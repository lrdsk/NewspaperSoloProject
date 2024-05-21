package org.example;

import org.example.dto.CommentDTO;
import org.example.models.Comment;
import org.example.models.Post;
import org.example.models.User;
import org.example.repositories.CommentRepository;
import org.example.repositories.PostRepository;
import org.example.repositories.UserRepository;
import org.example.services.servicesImpl.CommentServiceImpl;
import org.example.util.mappers.CommentMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class CommentServiceImplTests {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    public void testFindAllByPostId() {
        int postId = 1;
        Post post = new Post();

        Comment comment = new Comment();
        comment.setInfo("Test comment");
        comment.setPost(post);
        comment.setDatePublish(new Date());
        comment.setUser(new User());
        comment.setCommentId(1);
        post.setComments(Arrays.asList(comment));

        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        Mockito.when(commentMapper.toDto(comment)).thenReturn(new CommentDTO());

        List<CommentDTO> commentDTOList = commentService.findAllByPostId(postId);

        Assert.assertEquals(1, commentDTOList.size());
    }

    @Test
    public void testSaveComment() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setPostId(1);
        commentDTO.setUserEmail("test@example.com");

        Post post = new Post();
        User user = new User();
        user.setEmail(commentDTO.getUserEmail());

        Mockito.when(postRepository.findById(commentDTO.getPostId())).thenReturn(Optional.of(post));
        Mockito.when(userRepository.findByEmail(commentDTO.getUserEmail())).thenReturn(Optional.of(user));
        Mockito.when(commentMapper.toEntity(commentDTO)).thenReturn(new Comment());

        commentService.saveComment(commentDTO);

    }
}
