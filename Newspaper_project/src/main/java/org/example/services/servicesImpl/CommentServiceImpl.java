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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    public CommentServiceImpl(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository, CommentMapper commentMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    public List<CommentDTO> findAllByPostId(int postId){

        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        return post.getComments().stream().map(commentMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public void saveComment(CommentDTO commentDTO){
        Post post = postRepository.findById(commentDTO.getPostId()).orElseThrow(PostNotFoundException::new);
        User user = userRepository.findByEmail(commentDTO.getUserEmail()).orElseThrow(UserNotFoundException::new);

        Comment comment = commentMapper.toEntity(commentDTO);

        comment.setPost(post);
        comment.setUser(user);
        comment.setDatePublish(new Date());

        commentRepository.save(comment);
    }
}
