package org.example.services.servicesImpl;

import org.example.dto.AuthDTO;
import org.example.dto.UserDTO;
import org.example.models.User;
import org.example.security.JWTUtil;
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
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final RegistrationServiceImpl registrationService;
    private final UserValidator userValidator;
    private final JWTUtil jwtUtil;
    private final UserMapper userMapper;
    private final UserServiceImpl userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(RegistrationServiceImpl registrationService, UserValidator userValidator, UserMapper userMapper, AuthenticationManager authenticationManager, UserServiceImpl userService, JWTUtil jwtUtil) {
        this.registrationService = registrationService;
        this.userValidator = userValidator;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<Map<String, String>> performLogin(AuthDTO authDTO, BindingResult bindingResult){
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
            return new ResponseEntity<>(Collections.singletonMap("Error","Bad credentials in form"), HttpStatus.BAD_REQUEST);
        }
        String token = jwtUtil.generateToken(authDTO.getEmail());
        UserDTO userDTO = userService.findByEmail(authDTO.getEmail()).get();

        Map<String, String> response = new HashMap<>();
        response.put("jwt-token", token);
        response.put("name", userDTO.getName());
        response.put("surname", userDTO.getSurname());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Map<String, String>> performRegistration(UserDTO userDTO, BindingResult bindingResult){
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
}
