package org.example.controllers;

import org.example.dto.PostDTO;
import org.example.security.CustomUserDetails;
import org.example.servicesImpl.LikeServiceImpl;
import org.example.servicesImpl.PostServiceImpl;
import org.example.util.errorResponses.ErrorResponse;
import org.example.util.exceptions.PostNotCreatedException;
import org.example.util.exceptions.PostNotFoundException;
import org.example.util.mappers.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
    private final PostServiceImpl postService;
    private final LikeServiceImpl likeService;

    private final PostMapper postMapper;

    @Autowired
    public PostController(PostServiceImpl postService, LikeServiceImpl likeService, PostMapper postMapper) {
        this.postService = postService;
        this.likeService = likeService;
        this.postMapper = postMapper;
    }

    @GetMapping()
    public List<PostDTO> index(){
        return postService.findAll();
    }

    @GetMapping("/sort")
    public List<PostDTO> indexByDateDesc(){
        return postService.findAllByDateDesc();
    }

    @GetMapping("/{id}")
    public PostDTO getOne(@PathVariable("id") int id){
        return  postService.findOne(id);
    }
    @GetMapping("/{id}/like")
    public HttpEntity<HttpStatus> setLikeToPost(@PathVariable("id") int postId){ //todo: нужно реализовать отмену like и check if the post is worth liking
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        likeService.setLikeToPost(userDetails.getUsername(), postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //todo: добавить возможность получить количество лайков на посте

    //todo: добавить обработку и сохранение файла на сервере в методе
    @PostMapping()
    public HttpEntity<String> addPost(@RequestPart("photoFile") MultipartFile photoFile, @RequestPart("postDTO") @Valid PostDTO postDTO,
                                      BindingResult bindingResult){

        if(bindingResult.hasErrors()){
           String errorMsg = createErrorMessage(bindingResult);
           throw new PostNotCreatedException(errorMsg);
        }
        postService.save(postDTO);
        try (InputStream is = photoFile.getInputStream()) {
            return new ResponseEntity<>("Name:" + photoFile.getName()
                    + " File Name:" + photoFile.getOriginalFilename() + ", Size:" + is.available(), HttpStatus.OK);
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @DeleteMapping("/{id}")
    public HttpEntity<HttpStatus> deletePost(@PathVariable("id") int id){
        PostDTO post = postService.findOne(id);
        if(post != null)
            postService.deleteOne(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String createErrorMessage(BindingResult bindingResult){
        StringBuilder errorMsg = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for(FieldError error : errors){
            errorMsg.append(error.getField())
                    .append(" - ").append(error.getDefaultMessage())
                    .append("; ");
        }

        return errorMsg.toString();
    }
}
