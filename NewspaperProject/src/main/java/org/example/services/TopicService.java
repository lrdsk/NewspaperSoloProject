package org.example.services;

import org.example.dto.TopicDTO;
import org.example.models.Topic;

import java.util.List;

public interface TopicService {
    List<TopicDTO> findAll();
    Topic findByName(String name);
    void save(TopicDTO topicDTO);
}
