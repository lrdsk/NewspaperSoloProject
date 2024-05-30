package org.example.services.servicesImpl;

import org.example.dto.TopicDTO;
import org.example.repositories.TopicRepository;
import org.example.util.mappers.TopicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TopicServiceImpl {
    private final TopicRepository topicRepository;

    private final TopicMapper topicMapper;
    @Autowired
    public TopicServiceImpl(TopicRepository topicRepository, TopicMapper topicMapper) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
    }

    public List<TopicDTO> findAll(){
        return topicRepository.findAll().stream().map(topicMapper::toDto).collect(Collectors.toList());
    }
}
