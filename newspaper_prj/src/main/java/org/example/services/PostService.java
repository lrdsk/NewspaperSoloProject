package org.example.services;

import org.example.dto.PostDTO;

import java.util.List;
import java.util.Optional;

public interface PostService {
    List<PostDTO> findAll();
    PostDTO findOne(int id);
    void save(PostDTO postDTO);

    void deleteOne(int id);
}
