package org.example.services;

import org.example.dto.UserDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    List<UserDTO> findAll();
    UserDTO findById(int id);
    void deleteById(int id);
    Optional<UserDTO> findByEmail(String email);
    Set<Integer> getSetPostLiked(String email);
}
