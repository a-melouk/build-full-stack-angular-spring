package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.models.Topic;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between {@link Topic} entities and {@link TopicDto} Data Transfer Objects.
 */
@Component
public class TopicMapper {

    /**
     * Converts a {@link Topic} entity to a {@link TopicDto}.
     *
     * @param topic The {@link Topic} entity to convert.
     * @return The corresponding {@link TopicDto}, or null if the input is null.
     */
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

    /**
     * Converts a list of {@link Topic} entities to a list of {@link TopicDto}s.
     *
     * @param topics The list of {@link Topic} entities to convert.
     * @return A list of {@link TopicDto}s, or null if the input list is null.
     */
    public List<TopicDto> toDtoList(List<Topic> topics) {
        if (topics == null) {
            return null;
        }
        return topics.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Converts a {@link TopicDto} to a {@link Topic} entity.
     *
     * @param topicDto The {@link TopicDto} to convert.
     * @return The corresponding {@link Topic} entity, or null if the input is null.
     */
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