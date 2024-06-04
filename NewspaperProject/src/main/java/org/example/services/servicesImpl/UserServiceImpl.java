package org.example.services.servicesImpl;

import org.example.dto.TopicDTO;
import org.example.dto.UserDTO;
import org.example.models.*;
import org.example.repositories.UserRepository;
import org.example.repositories.UserSelectedTopicRepository;
import org.example.security.JWTUtil;
import org.example.services.UserService;
import org.example.util.exceptions.UserNotFoundException;
import org.example.util.mappers.TopicMapper;
import org.example.util.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final TopicServiceImpl topicService;
    private final UserSelectedTopicRepository userSelectedTopicRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, JWTUtil jwtUtil, TopicMapper topicMapper, TopicServiceImpl topicService, UserSelectedTopicRepository userSelectedTopicRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.topicMapper = topicMapper;
        this.topicService = topicService;
        this.userSelectedTopicRepository = userSelectedTopicRepository;
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
    public Set<Integer> getSetPostLiked(String token) {
        String jwtToken = token.substring(7);
        String email = jwtUtil.validateTokenAndRetrieveClaim(jwtToken);

        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return user.getPosts().stream().map(Post::getPostId).collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void setSelectedTopics(List<TopicDTO> topicsDTO, String token, int status) {
        String jwtToken = token.substring(7);
        String email = jwtUtil.validateTokenAndRetrieveClaim(jwtToken);

        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        Set<UserSelectedTopic> userSelectedTopicsList = user.getSelectedTopics();

        for (TopicDTO topicDTO : topicsDTO) {
            Topic topic = topicService.findByName(topicDTO.getName());

            UserSelectedTopicId userSelectedTopicId = new UserSelectedTopicId(user.getUserId(), topic.getTopicId());

            if (!userSelectedTopicRepository.existsById(userSelectedTopicId)) {
                UserSelectedTopic userSelectedTopic = new UserSelectedTopic();

                userSelectedTopic.setId(userSelectedTopicId);
                userSelectedTopic.setStatus(status);

                userSelectedTopic.setUser(user);
                userSelectedTopic.setTopic(topic);
                userSelectedTopicRepository.save(userSelectedTopic);

                userSelectedTopicsList.add(userSelectedTopic);
            }
        }

        user.setSelectedTopics(userSelectedTopicsList);
        userRepository.save(user);
    }


}