package org.example.services;

import org.example.dto.PostDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    List<PostDTO> findAll();
    PostDTO findOne(int id);
    void save(MultipartFile multipartFile, PostDTO postDTO) throws IOException;
    void deleteOne(int id);
    List<PostDTO> findALlWithoutBanned(String token);
    List<PostDTO> findAllByDateDesc();
    List<PostDTO> findAllByUserFavorites(String token);
}
