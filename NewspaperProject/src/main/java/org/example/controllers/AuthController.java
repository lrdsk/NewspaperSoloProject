package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.dto.AuthDTO;
import org.example.dto.UserDTO;
import org.example.models.User;
import org.example.security.JWTUtil;
import org.example.services.servicesImpl.AuthService;
import org.example.services.servicesImpl.RegistrationServiceImpl;
import org.example.services.servicesImpl.UserServiceImpl;
import org.example.util.errorResponses.ErrorMessage;
import org.example.util.exceptions.AuthFormIncorrectException;
import org.example.util.exceptions.UserIncorrectException;
import org.example.util.mappers.UserMapper;
import org.example.util.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Регистрация нового пользователя")
    @PostMapping("/registration")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно авторизован, вернется jwt-token")
    @ApiResponse(responseCode = "400", description = "Неорретно введены данные для регистрации")
    public ResponseEntity<Map<String, String>> performRegistration(@RequestBody @Valid UserDTO userDTO,
                                                                   BindingResult bindingResult){
        return authService.performRegistration(userDTO, bindingResult);
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/login")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно авторизован, вернется jwt-token и данные пользователя (name, surname)")
    @ApiResponse(responseCode = "400", description = "Неорретно введены данные для авторизации или они недействительны")
    public ResponseEntity<Map<String, String>> performLogin(@RequestBody @Valid AuthDTO authDTO,
                                               BindingResult bindingResult){
       return authService.performLogin(authDTO, bindingResult);
    }
}
