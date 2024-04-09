package org.example.controllers;

import org.example.dto.PostDTO;
import org.example.servicesImpl.PostServiceImpl;
import org.example.util.errorResponses.ErrorResponse;
import org.example.util.exceptions.PostNotCreatedException;
import org.example.util.exceptions.PostNotFoundException;
import org.example.util.mappers.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
    private final PostServiceImpl postService;

    private final PostMapper postMapper;

    @Autowired
    public PostController(PostServiceImpl postService, PostMapper postMapper) {
        this.postService = postService;
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

    @PostMapping()
    public HttpEntity<HttpStatus> addPost(@RequestBody @Valid PostDTO postDTO, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
           String errorMsg = createErrorMessage(bindingResult);
           throw new PostNotCreatedException(errorMsg);
        }
        postService.save(postDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<HttpStatus> deletePost(@PathVariable("id") int id){
        PostDTO post = postService.findOne(id);
        if(post != null)
            postService.deleteOne(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(PostNotFoundException postNotFoundException){
        ErrorResponse response = new ErrorResponse(
                "Post with this id wasn't found",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(PostNotCreatedException postNotCreatedException){
        ErrorResponse response = new ErrorResponse(
                postNotCreatedException.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
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
