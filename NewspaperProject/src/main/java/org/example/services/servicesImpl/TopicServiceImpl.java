package org.example.services.servicesImpl;

import org.example.dto.TopicDTO;
import org.example.models.Topic;
import org.example.repositories.TopicRepository;
import org.example.services.TopicService;
import org.example.util.mappers.TopicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TopicServiceImpl implements TopicService {
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

    public Topic findByName(String name){
        return topicRepository.findByName(name);
    }
    @Transactional
    public void save(TopicDTO topicDTO){
        Topic topic = topicMapper.toEntity(topicDTO);
        topicRepository.save(topic);
    }
}
