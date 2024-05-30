package org.example.util.mappers;

import org.example.dto.PostDTO;
import org.example.dto.TopicDTO;
import org.example.models.Post;
import org.example.models.Topic;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TopicMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public TopicMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Topic toEntity(TopicDTO topicDTO){
        return Objects.isNull(topicDTO) ? null : modelMapper.map(topicDTO, Topic.class);
    }

    public TopicDTO toDto(Topic topic){
        return Objects.isNull(topic) ? null : modelMapper.map(topic, TopicDTO.class);
    }
}
