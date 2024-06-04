package org.example.services.servicesImpl;

import org.example.dto.PostDTO;
import org.example.dto.TopicDTO;
import org.example.models.Post;
import org.example.models.Topic;
import org.example.models.User;
import org.example.models.UserSelectedTopic;
import org.example.repositories.PostRepository;
import org.example.repositories.UserRepository;
import org.example.security.JWTUtil;
import org.example.services.PostService;
import org.example.util.errorResponses.ErrorMessage;
import org.example.util.exceptions.PostNotCreatedException;
import org.example.util.exceptions.PostNotFoundException;
import org.example.util.exceptions.UserNotFoundException;
import org.example.util.mappers.PostMapper;
import org.example.util.mappers.TopicMapper;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final MultipartServiceImpl multipartService;
    private final TopicServiceImpl topicService;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper, MultipartServiceImpl multipartService, TopicServiceImpl topicService, JWTUtil jwtUtil, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.multipartService = multipartService;
        this.topicService = topicService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public List<PostDTO> findAll() {
        return postRepository.findAll().stream().map(postMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> findALlWithoutBanned(String token){
        String jwtToken = token.substring(7);
        String email = jwtUtil.validateTokenAndRetrieveClaim(jwtToken);
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        return postRepository.findAllPostsExceptSelectedTopics(user.getUserId()).stream().map(postMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> findAllByDateDesc(){
        return postRepository.findAllByDateDesc().stream().map(postMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> findAllByUserFavorites(String token){
        String jwtToken = token.substring(7);
        String email = jwtUtil.validateTokenAndRetrieveClaim(jwtToken);
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        List<Post> posts = postRepository.findPostsByUserId(user.getUserId());
        return posts.stream().map(postMapper::toDto).collect(Collectors.toList());
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
        Post post = postMapper.toEntity(postDTO);
        List<TopicDTO> topicDTOS = postDTO.getTopicDTOList();
        List<Topic> topics = new ArrayList<>();

        for (TopicDTO topicDTO : topicDTOS) {
            Topic currTopic = topicService.findByName(topicDTO.getName());
            if (currTopic == null) {
               topicService.save(topicDTO);
                currTopic = topicService.findByName(topicDTO.getName());
            }

            if (!topics.contains(currTopic)) {
                topics.add(currTopic);
            }
        }

        post.setPostTopicsList(topics);
        postRepository.save(post);


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