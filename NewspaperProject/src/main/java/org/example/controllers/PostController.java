package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.dto.CommentDTO;
import org.example.dto.PostDTO;
import org.example.services.servicesImpl.CommentServiceImpl;
import org.example.services.servicesImpl.LikeServiceImpl;
import org.example.services.servicesImpl.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
    private final PostServiceImpl postService;
    private final LikeServiceImpl likeService;
    private final CommentServiceImpl commentService;


    @Autowired
    public PostController(PostServiceImpl postService, LikeServiceImpl likeService,
                          CommentServiceImpl commentService) {
        this.postService = postService;
        this.likeService = likeService;
        this.commentService = commentService;
    }

    @Operation(summary = "Получить все посты в виде списка, отсортированные по дате")
    @GetMapping()
    public List<PostDTO> indexByDateDesc(){
        return postService.findAllByDateDesc();
    }


    @Operation(summary = "Получить все посты в виде списка, кроме тех, которые отмечены заблокированными")
    @GetMapping("/index")
    public List<PostDTO> indexWithoutBanned(@RequestHeader(name = "Authorization") String token){
        return postService.findALlWithoutBanned(token);
    }

    @Operation(summary = "Получить все посты в виде списка, которые отмечены любимыми (в порядке, чем больше в посте тем, отмеченных любимыми, тем первее стоит пост)")
    @GetMapping("/favorites")
    public List<PostDTO> indexFavoritesPosts(@RequestHeader(name = "Authorization") String token){
        return postService.findAllByUserFavorites(token);
    }

    @Operation(summary = "Получить пост по заданному id")
    @GetMapping("/{id}")
    public PostDTO getOne(@PathVariable("id") int id)
            throws IOException {
        return postService.findOne(id);

    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<Resource> getResourceFile(@PathVariable("id") int id, HttpServletRequest request) throws IOException {
        return postService.getResourceFile(id,request);
    }

    @Operation(summary = "По id поста поставить лайк при авторизированном пользователе",
            description = "Нужно отправлять header authorization Bearer token")
    @PostMapping("/{id}/like")
    @ApiResponse(responseCode = "200", description = "Like успешно добавлен")
    @ApiResponse(responseCode = "403", description = "Не отправлен header authorization Bearer token")
    @ApiResponse(responseCode = "500", description = "Invalid jwt-token to authorization")
    public HttpEntity<Boolean> setLikeToPost(@PathVariable("id") int postId){
       return likeService.setLikeToPost(postId);
    }

    @Operation(summary = "Получить количество лайков на посте по его id")
    @GetMapping("/{id}/like/count")
    public int getCountLikes(@PathVariable("id") int postId){
        return likeService.getCountLikes(postId);
    }

    @Operation(summary = "Добавить новый пост, а так же темы, которые ему соответствуют ",
            description = "Может только человек с ролью админ")
    @PostMapping()
    @PreAuthorize("ADMIN")
    public HttpEntity<String> addPost(
                                          @RequestPart("photoFile") MultipartFile photoFile,
                                      @RequestPart("postDTO") @Valid PostDTO postDTO,
                                      BindingResult bindingResult) {

        return postService.addPost(photoFile, postDTO, bindingResult);
    }

    @Operation(summary = "Добавления комментария к посту по его id",
            description = "Нужно отправлять header authorization Bearer token")
    @PostMapping("/{postId}")
    @ApiResponse(responseCode = "200", description = "Комментарий успешно добавлен")
    @ApiResponse(responseCode = "403", description = "Не отправлен header authorization Bearer token")
    @ApiResponse(responseCode = "400", description = "Не правильная форма комментария")
    @ApiResponse(responseCode = "500", description = "Invalid jwt-token to authorization")
    public HttpEntity<HttpStatus> addComment(@RequestHeader(name = "Authorization") String token, @PathVariable("postId") int postId,
                                             @RequestBody CommentDTO commentDTO, BindingResult bindingResult){
        CommentDTO commentWithEmail = commentService.setUserEmailToComment(token, postId, commentDTO, bindingResult);
        return commentService.saveComment(commentWithEmail);
    }


    @Operation(summary = "Получить все комменарии к посту по его id")
    @GetMapping("/{postId}/comments")
    public List<CommentDTO> indexComments(@PathVariable("postId") int postId){
        return commentService.findAllByPostId(postId);
    }

}
