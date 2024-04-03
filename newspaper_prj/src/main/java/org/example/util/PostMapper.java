package org.example.util;

import org.example.dto.PostDTO;
import org.example.models.Post;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PostMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public PostMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Post toEntity(PostDTO postDTO){
        return Objects.isNull(postDTO) ? null : modelMapper.map(postDTO, Post.class);
    }

    public PostDTO toDto(Post post){
        return Objects.isNull(post) ? null : modelMapper.map(post, PostDTO.class);
    }
}