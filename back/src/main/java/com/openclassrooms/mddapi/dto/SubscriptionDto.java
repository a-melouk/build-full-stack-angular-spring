package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for subscription information.
 * This class represents a user's subscription to a topic, including details about the topic.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDto {
    /**
     * The unique identifier of the subscription.
     */
    private Long id;

    /**
     * The ID of the user who is subscribed.
     */
    private Long userId;

    /**
     * The ID of the topic to which the user is subscribed.
     */
    private Long topicId;

    /**
     * The name of the subscribed topic.
     */
    private String topicName;

    /**
     * The description of the subscribed topic.
     */
    private String topicDescription;

    /**
     * The timestamp when the subscription was created.
     */
    private LocalDateTime subscribedAt;
}