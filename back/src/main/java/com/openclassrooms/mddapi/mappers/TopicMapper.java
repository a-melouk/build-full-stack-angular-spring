package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.models.Topic;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TopicMapper {

    public TopicDto toDto(Topic topic) {
        if (topic == null) {
            return null;
        }

        return TopicDto.builder()
                .id(topic.getId())
                .name(topic.getName())
                .description(topic.getDescription())
                .build();
    }

    public List<TopicDto> toDtoList(List<Topic> topics) {
        if (topics == null) {
            return null;
        }
        return topics.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Topic toEntity(TopicDto topicDto) {
        if (topicDto == null) {
            return null;
        }

        return Topic.builder()
                .name(topicDto.getName())
                .description(topicDto.getDescription())
                .build();
    }

}