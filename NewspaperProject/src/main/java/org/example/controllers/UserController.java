package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.dto.TopicDTO;
import org.example.security.JWTUtil;
import org.example.services.servicesImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all user likes by his email")
    @GetMapping("/likes")
    @ApiResponse(responseCode = "200", description = "List of id posts is liked")
    @ApiResponse(responseCode = "400", description = "Не отправлен header authorization Bearer token")
    @ApiResponse(responseCode = "403", description = "Valid jwt, but insufficient access rights")
    public Set<Integer> getLikes(@RequestHeader(name = "Authorization") String token){
        return userService.getSetPostLiked(token);
    }

    @Operation(summary = "Добавить пользователю любимую тему, можно отправлять только уже существующие")
    @PostMapping("/favorite_topic")
    public HttpEntity<HttpStatus> setFavoriteTopics(@RequestHeader(name = "Authorization") String token,
                                                    @RequestBody List<TopicDTO> topicDTO){
        userService.setSelectedTopics(topicDTO, token, 1);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Добавить пользователю заблокированную тему, можно отправлять только уже существующие")
    @PostMapping("/banned_topic")
    public HttpEntity<HttpStatus> setBannedTopics(@RequestHeader(name = "Authorization") String token,
                                                  @RequestBody List<TopicDTO> topicDTO){
        userService.setSelectedTopics(topicDTO, token, 2);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
