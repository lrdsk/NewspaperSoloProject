package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.dto.AuthDTO;
import org.example.dto.UserDTO;
import org.example.services.servicesImpl.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
