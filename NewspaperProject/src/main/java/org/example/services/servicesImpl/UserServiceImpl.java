package org.example.services.servicesImpl;

import org.example.dto.TopicDTO;
import org.example.dto.UserDTO;
import org.example.models.Post;
import org.example.models.Topic;
import org.example.models.User;
import org.example.repositories.UserRepository;
import org.example.security.JWTUtil;
import org.example.services.UserService;
import org.example.util.exceptions.UserNotFoundException;
import org.example.util.mappers.TopicMapper;
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
    private final JWTUtil jwtUtil;
    private final TopicMapper topicMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, JWTUtil jwtUtil, TopicMapper topicMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.topicMapper = topicMapper;
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

    @Override
    public Set<Integer> getSetPostLiked(String token){
        String jwtToken = token.substring(7);
        String email = jwtUtil.validateTokenAndRetrieveClaim(jwtToken);

        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return user.getPosts().stream().map(Post::getPostId).collect(Collectors.toSet());
    }

    @Transactional
    public void setSelectedTopics(List<TopicDTO> topicsDTO, String token){
        String jwtToken = token.substring(7);
        String email = jwtUtil.validateTokenAndRetrieveClaim(jwtToken);

        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        List<Topic> topics = topicsDTO.stream().map(topicMapper::toEntity).collect(Collectors.toList());

        user.setTopics(topics);
    }

    @Transactional
    public void setBannedTopics(List<TopicDTO> topicsDTO, String token){
        String jwtToken = token.substring(7);
        String email = jwtUtil.validateTokenAndRetrieveClaim(jwtToken);

        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        List<Topic> topics = topicsDTO.stream().map(topicMapper::toEntity).collect(Collectors.toList());

        user.setBannedTopics(topics);
    }

}
