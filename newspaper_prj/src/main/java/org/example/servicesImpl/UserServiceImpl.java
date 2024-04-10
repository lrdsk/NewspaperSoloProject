package org.example.servicesImpl;

import org.example.dto.PostDTO;
import org.example.dto.UserDTO;
import org.example.models.User;
import org.example.repositories.UserRepository;
import org.example.services.UserService;
import org.example.util.exceptions.UserNotFoundException;
import org.example.util.mappers.PostMapper;
import org.example.util.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PostMapper postMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PostMapper postMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.postMapper = postMapper;
    }

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO findById(int userId) {
        return userMapper.toDto(userRepository.findById(userId).orElseThrow(UserNotFoundException::new));
    }

    @Override
    @Transactional
    public void deleteById(int userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Optional<UserDTO> findByEmail(String email) {
        return Optional.ofNullable(userMapper.toDto(userRepository.findByEmail(email).orElse(null)));
    }

   /* public Set<PostDTO> findPostLiked(int userId){
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return user.getPosts().stream().map(postMapper::toDto).collect(Collectors.toSet());
    }*/
}
