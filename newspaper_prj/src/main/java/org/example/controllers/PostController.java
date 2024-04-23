package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javafx.geometry.Pos;
import org.example.dto.CommentDTO;
import org.example.dto.PostDTO;
import org.example.security.CustomUserDetails;
import org.example.servicesImpl.CommentServiceImpl;
import org.example.servicesImpl.LikeServiceImpl;
import org.example.servicesImpl.MultipartServiceImpl;
import org.example.servicesImpl.PostServiceImpl;
import org.example.util.errorResponses.ErrorMessage;
import org.example.util.exceptions.PostNotCreatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
    private final PostServiceImpl postService;
    private final LikeServiceImpl likeService;
    private final CommentServiceImpl commentService;
    private final MultipartServiceImpl multipartService;

    @Autowired
    public PostController(PostServiceImpl postService, LikeServiceImpl likeService, CommentServiceImpl commentService, MultipartServiceImpl multipartService) {
        this.postService = postService;
        this.likeService = likeService;
        this.commentService = commentService;
        this.multipartService = multipartService;
    }

    @Operation(summary = "Получить все посты в виде списка")
    @GetMapping()
    public List<PostDTO> indexByDateDesc(){
        return postService.findAllByDateDesc();
    }

    @Operation(summary = "Получить пост по заданному id")
    @GetMapping("/{id}")
    public PostDTO getOne(@PathVariable("id") int id)
            throws IOException {
        return postService.findOne(id);

    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<Resource> getResourceFile(
            @PathVariable("id") int id, HttpServletRequest request)
            throws IOException {
        PostDTO postDTO = postService.findOne(id);
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

    @Operation(summary = "По id поста поставить лайк при авторизированном пользователе",
            description = "Нужно отправлять header authorization Bearer token")
    @PostMapping("/{id}/like")
    @ApiResponse(responseCode = "200", description = "Like успешно добавлен")
    @ApiResponse(responseCode = "403", description = "Не отправлен header authorization Bearer token")
    @ApiResponse(responseCode = "500", description = "Invalid jwt-token to authorization")
    public HttpEntity<Boolean> setLikeToPost(@PathVariable("id") int postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        boolean isLiked = likeService.setLikeToPost(userDetails.getUsername(), postId);
        return new ResponseEntity<>(isLiked, HttpStatus.OK);
    }

    @Operation(summary = "Получить количество лайков на посте по его id")
    @GetMapping("/{id}/like/count")
    public int getCountLikes(@PathVariable("id") int postId){
        return likeService.getCountLikes(postId);
    }

    //todo: добавить обработку и сохранение файла на сервере в методе
    @Operation(summary = "Добавить новый пост ",
            description = "Может только человек с ролью админ")
    @PostMapping()
    @PreAuthorize("ADMIN")
    public HttpEntity<String> addPost(
                                          @RequestPart("photoFile") MultipartFile photoFile,
                                      @RequestPart("postDTO") @Valid PostDTO postDTO,
                                      BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
           String errorMsg = ErrorMessage.createErrorMessage(bindingResult);
           throw new PostNotCreatedException(errorMsg);
        }
        try {
            postService.save(photoFile, postDTO);
            return new ResponseEntity<>("Post saved", HttpStatus.OK);
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Operation(summary = "Добавления комментария к посту по его id",
            description = "Нужно отправлять header authorization Bearer token")
    @PostMapping("/{postId}")
    @ApiResponse(responseCode = "200", description = "Комментарий успешно добавлен")
    @ApiResponse(responseCode = "403", description = "Не отправлен header authorization Bearer token")
    @ApiResponse(responseCode = "400", description = "Не правильная форма комментария")
    @ApiResponse(responseCode = "500", description = "Invalid jwt-token to authorization")
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


    @Operation(summary = "Получить все комменарии к послту по его id")
    @GetMapping("/{postId}/comments")
    public List<CommentDTO> indexComments(@PathVariable("postId") int postId){
        return commentService.findAllByPostId(postId);
    }

   /* @DeleteMapping("/{id}")
    public HttpEntity<HttpStatus> deletePost(@PathVariable("id") int id){
        PostDTO post = postService.findOne(id);
        if(post != null)
            postService.deleteOne(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }*/
}
