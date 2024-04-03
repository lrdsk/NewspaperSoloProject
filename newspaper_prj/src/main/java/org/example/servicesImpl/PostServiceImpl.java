package org.example.servicesImpl;

import org.example.dto.PostDTO;
import org.example.services.PostService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Override
    public List<PostDTO> findAll() {
        return null;
    }

    @Override
    public PostDTO findOne(int id) {
        return null;
    }

    @Override
    public void save(PostDTO postDTO) {

    }

    @Override
    public void deleteOne(int id) {

    }
}
