package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.security.JWTUtil;
import org.example.servicesImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserServiceImpl userService;
    private final JWTUtil jwtUtil;

    @Autowired
    public UserController(UserServiceImpl userService, JWTUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /*@GetMapping()
    public List<UserDTO> index(){
        return userService.findAll();
    }*/
/*
    @GetMapping("/{id}")
    public UserDTO getOne(@PathVariable("id") int userId){
        return userService.findById(userId);
    }*/

    @Operation(summary = "Get all user likes by his email")
    @GetMapping("/likes")
    @ApiResponse(responseCode = "200", description = "List of id posts is liked")
    @ApiResponse(responseCode = "400", description = "Не отправлен header authorization Bearer token")
    @ApiResponse(responseCode = "403", description = "Valid jwt, but insufficient access rights")
    public Set<Integer> getLikes(@RequestHeader(name = "Authorization") String token){
        String jwtToken = token.substring(7);
        String email = jwtUtil.validateTokenAndRetrieveClaim(jwtToken);
        if(email == null){
            System.out.println("EMAIL IS NULL");
            System.out.println("EMAIL IS NULL");
            System.out.println("EMAIL IS NULL");
        }
        return userService.getSetPostLiked(email);
    }
/*    @DeleteMapping("/{id}")
    public HttpEntity<HttpStatus> deleteUser(@PathVariable("id") int id){
        UserDTO userDTO = userService.findById(id);

        if(userDTO != null)
            userService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }*/

}
