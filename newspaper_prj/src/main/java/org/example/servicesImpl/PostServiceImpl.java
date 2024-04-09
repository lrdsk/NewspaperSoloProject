package org.example.servicesImpl;

import org.example.dto.PostDTO;
import org.example.repositories.PostRepository;
import org.example.services.PostService;
import org.example.util.exceptions.PostNotFoundException;
import org.example.util.mappers.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Override
    public List<PostDTO> findAll() {
        return postRepository.findAll().stream().map(postMapper::toDto).collect(Collectors.toList());
    }

    public List<PostDTO> findAllByDateDesc(){
        return postRepository.findAllByDateDesc().stream().map(postMapper::toDto).collect(Collectors.toList());
    }
    @Override
    public PostDTO findOne(int id) {
        return postMapper.toDto(postRepository.findById(id).orElseThrow(PostNotFoundException::new));
    }

    @Override
    public void save(PostDTO postDTO) {
        postDTO.setDatePublish(new Date());

        postRepository.save(postMapper.toEntity(postDTO));
    }

    @Override
    public void deleteOne(int id) {
        postRepository.deleteById(id);
    }
}
