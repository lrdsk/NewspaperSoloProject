package org.example.services.servicesImpl;

import org.example.dto.PostDTO;
import org.example.repositories.PostRepository;
import org.example.services.PostService;
import org.example.util.errorResponses.ErrorMessage;
import org.example.util.exceptions.PostNotCreatedException;
import org.example.util.exceptions.PostNotFoundException;
import org.example.util.mappers.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
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
    @Transactional(readOnly = false)
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

    public ResponseEntity<Resource> getResourceFile(int id, HttpServletRequest request) throws IOException {
        PostDTO postDTO = findOne(id);
        Resource resource = multipartService.loadFileAsResource(postDTO.getPhoto());

        String contentType;
        contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @Transactional
    public HttpEntity<String> addPost(MultipartFile photoFile, PostDTO postDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            String errorMsg = ErrorMessage.createErrorMessage(bindingResult);
            throw new PostNotCreatedException(errorMsg);
        }
        try {
            save(photoFile, postDTO);
            return new ResponseEntity<>("Post saved", HttpStatus.OK);
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
