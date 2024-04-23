package org.example.services;

import org.example.dto.PostDTO;
import org.example.servicesImpl.MultipartServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PostService {
    List<PostDTO> findAll();
    PostDTO findOne(int id);
    void save(MultipartFile multipartFile, PostDTO postDTO) throws IOException;

    void deleteOne(int id);
}
