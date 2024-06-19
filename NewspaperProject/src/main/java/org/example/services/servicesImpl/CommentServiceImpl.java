package org.example.services.servicesImpl;

import org.example.dto.CommentDTO;
import org.example.models.Comment;
import org.example.models.Post;
import org.example.models.User;
import org.example.repositories.CommentRepository;
import org.example.repositories.PostRepository;
import org.example.repositories.UserRepository;
import org.example.util.exceptions.PostNotFoundException;
import org.example.util.exceptions.UserNotFoundException;
import org.example.util.mappers.CommentMapper;
import org.example.util.mappers.JWTTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CommentServiceImpl {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final JWTTokenMapper jwtTokenMapper;

    @Autowired
    public CommentServiceImpl(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository,
                              CommentMapper commentMapper, JWTTokenMapper jwtTokenMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.jwtTokenMapper = jwtTokenMapper;
    }

    public List<CommentDTO> findAllByPostId(int postId){
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        return post.getComments().stream().map(commentMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public HttpEntity<HttpStatus> saveComment(CommentDTO commentDTO){
        if(commentDTO == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Post post = postRepository.findById(commentDTO.getPostId()).orElseThrow(PostNotFoundException::new);
        User user = userRepository.findByEmail(commentDTO.getUserEmail()).orElseThrow(UserNotFoundException::new);

        Comment comment = commentMapper.toEntity(commentDTO);

        comment.setPost(post);
        comment.setUser(user);
        comment.setDatePublish(new Date());

        commentRepository.save(comment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public CommentDTO setUserEmailToComment(String token, int postId, CommentDTO commentDTO, BindingResult bindingResult){
        String email = jwtTokenMapper.getEmailFromToken(token);
        commentDTO.setUserEmail(email);
        commentDTO.setPostId(postId);

        if(bindingResult.hasErrors()){
            return null;
        }

        return commentDTO;
    }
}
