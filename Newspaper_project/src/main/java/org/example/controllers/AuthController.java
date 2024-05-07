package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.dto.AuthDTO;
import org.example.dto.UserDTO;
import org.example.models.User;
import org.example.security.JWTUtil;
import org.example.servicesImpl.RegistrationServiceImpl;
import org.example.servicesImpl.UserServiceImpl;
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
    private final RegistrationServiceImpl registrationService;
    private final UserValidator userValidator;
    private final JWTUtil jwtUtil;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userService;

    @Autowired
    public AuthController(RegistrationServiceImpl registrationService, UserValidator userValidator, JWTUtil jwtUtil, UserMapper userMapper, AuthenticationManager authenticationManager, UserServiceImpl userService) {
        this.registrationService = registrationService;
        this.userValidator = userValidator;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }
    @Operation(summary = "Регистрация нового пользователя")
    @PostMapping("/registration")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно авторизован, вернется jwt-token")
    @ApiResponse(responseCode = "400", description = "Неорретно введены данные для регистрации")
    public ResponseEntity<Map<String, String>> performRegistration(@RequestBody @Valid UserDTO userDTO,
                                                   BindingResult bindingResult){
        User user = userMapper.toEntity(userDTO);
        userValidator.validate(user, bindingResult);

        if(bindingResult.hasErrors()){
            String errorMsg = ErrorMessage.createErrorMessage(bindingResult);
            throw new UserIncorrectException(errorMsg);
        }

        registrationService.register(user);
        String token = jwtUtil.generateToken(user.getEmail());
        return new ResponseEntity<>(Collections.singletonMap("jwt-token", token), HttpStatus.OK);
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/login")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно авторизован, вернется jwt-token и данные пользователя (name, surname)")
    @ApiResponse(responseCode = "400", description = "Неорретно введены данные для авторизации или они недействительны")
    public ResponseEntity<Map<String, String>> performLogin(@RequestBody @Valid AuthDTO authDTO,
                                               BindingResult bindingResult){
        if(bindingResult.hasErrors()){
           String errorMsg = ErrorMessage.createErrorMessage(bindingResult);
           throw new AuthFormIncorrectException(errorMsg);
        }

        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authDTO.getEmail(),
                        authDTO.getPassword());
        try {
            authenticationManager.authenticate(authInputToken);
        }catch (BadCredentialsException ex){
            return new ResponseEntity<>(Collections.singletonMap("Error","Bad credentials in form"),HttpStatus.BAD_REQUEST);
        }
        String token = jwtUtil.generateToken(authDTO.getEmail());
        UserDTO userDTO = userService.findByEmail(authDTO.getEmail()).get();

        Map<String, String> response = new HashMap<>();
        response.put("jwt-token", token);
        response.put("name", userDTO.getName());
        response.put("surname", userDTO.getSurname());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
