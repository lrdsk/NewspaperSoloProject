package org.example.services;

import org.example.dto.UserDTO;
import org.example.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> findAll();
    Optional<UserDTO> findById(int id);
    void deleteById(int id);
    Optional<UserDTO> findByEmail(String email);
}
