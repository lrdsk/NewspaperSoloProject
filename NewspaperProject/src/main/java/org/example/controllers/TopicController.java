package org.example.controllers;

import org.example.dto.TopicDTO;
import org.example.models.Topic;
import org.example.services.servicesImpl.TopicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/topic")
public class TopicController {
    private final TopicServiceImpl topicService;

    @Autowired
    public TopicController(TopicServiceImpl topicService) {
        this.topicService = topicService;
    }

    public List<TopicDTO> index(){
        return topicService.findAll();
    }
}
