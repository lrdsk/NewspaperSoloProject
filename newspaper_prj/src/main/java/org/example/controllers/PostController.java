package org.example.controllers;

import org.example.dto.CommentDTO;
import org.example.dto.PostDTO;
import org.example.security.CustomUserDetails;
import org.example.servicesImpl.CommentServiceImpl;
import org.example.servicesImpl.LikeServiceImpl;
import org.example.servicesImpl.PostServiceImpl;
import org.example.util.exceptions.PostNotCreatedException;
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
    private final CommentServiceImpl commentService;

    @Autowired
    public PostController(PostServiceImpl postService, LikeServiceImpl likeService, CommentServiceImpl commentService) {
        this.postService = postService;
        this.likeService = likeService;
        this.commentService = commentService;
    }

    @GetMapping()
    public List<PostDTO> indexByDateDesc(){
        return postService.findAllByDateDesc();
    }

    @GetMapping("/{id}")
    public PostDTO getOne(@PathVariable("id") int id){
        return  postService.findOne(id);
    }
    @PostMapping("/{id}/like")
    public HttpEntity<Boolean> setLikeToPost(@PathVariable("id") int postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        boolean isLiked = likeService.setLikeToPost(userDetails.getUsername(), postId);
        return new ResponseEntity<>(isLiked, HttpStatus.OK);
    }

    @GetMapping("/{id}/like/count")
    public int getCountLikes(@PathVariable("id") int postId){
        return likeService.getCountLikes(postId);
    }

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

    @PostMapping("/{postId}")
    public HttpEntity<HttpStatus> addComment(@PathVariable("postId") int postId,
                                             @RequestBody CommentDTO commentDTO, BindingResult bindingResult){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        commentDTO.setUserEmail(userDetails.getUsername());
        commentDTO.setPostId(postId);

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        commentService.saveComment(commentDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/{postId}/comments")
    public List<CommentDTO> index(@PathVariable("postId") int postId){
        return commentService.findAllByPostId(postId);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<HttpStatus> deletePost(@PathVariable("id") int id){
        PostDTO post = postService.findOne(id);
        if(post != null)
            postService.deleteOne(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String createErrorMessage(BindingResult bindingResult){ //todo: вынести в отдельный класс component
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
