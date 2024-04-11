package org.example.util.mappers;

import org.example.dto.CommentDTO;
import org.example.dto.PostDTO;
import org.example.models.Comment;
import org.example.models.Post;
import org.example.servicesImpl.PostServiceImpl;
import org.example.servicesImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CommentMapper {
    private final UserServiceImpl userService;
    private final PostServiceImpl postService;
    private final UserMapper userMapper;
    private final PostMapper postMapper;

    @Autowired
    public CommentMapper(UserServiceImpl userService, PostServiceImpl postService, UserMapper userMapper, PostMapper postMapper) {
        this.userService = userService;
        this.postService = postService;
        this.userMapper = userMapper;
        this.postMapper = postMapper;
    }

    public Comment toEntity(CommentDTO commentDTO){
        if(userService.findByEmail(commentDTO.getUserEmail()).isPresent()){
            Comment comment = new Comment();
            comment.setInfo(commentDTO.getInfo());
            comment.setUser(userMapper.toEntity(userService.findByEmail(commentDTO.getUserEmail()).get()));
            comment.setPost(postMapper.toEntity(postService.findOne(commentDTO.getPostId())));
            comment.setDatePublish(commentDTO.getDatePublish());
            return comment;
        }else{
            return null;
        }
    }

    public CommentDTO toDto(Comment comment){
        if(Objects.isNull(comment)){
            return null;
        }else{
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setInfo(comment.getInfo());
            commentDTO.setPostId(comment.getPost().getPostId());
            commentDTO.setUserEmail(comment.getUser().getEmail());
            commentDTO.setDatePublish(comment.getDatePublish());
            commentDTO.setName(comment.getUser().getName());
            commentDTO.setSurname(comment.getUser().getSurname());
            return commentDTO;
        }
    }
}
