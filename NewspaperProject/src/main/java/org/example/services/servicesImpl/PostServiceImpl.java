package org.example.services.servicesImpl;

import org.example.dto.PostDTO;
import org.example.repositories.PostRepository;
import org.example.services.PostService;
import org.example.util.exceptions.PostNotFoundException;
import org.example.util.mappers.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final MultipartServiceImpl multipartService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper, MultipartServiceImpl multipartService) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.multipartService = multipartService;
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
    @Transactional
    public void save(MultipartFile photoFile, PostDTO postDTO) throws IOException {
        String fileLocation = multipartService.savePhoto(photoFile);
        postDTO.setDatePublish(new Date());
        postDTO.setPhoto(fileLocation);
        postRepository.save(postMapper.toEntity(postDTO));
    }

    @Override
    public void deleteOne(int id) {
        postRepository.deleteById(id);
    }
}
