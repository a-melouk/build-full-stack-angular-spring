package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.SubscriptionDto;
import java.util.List;

/**
 * Service interface for subscription-related operations.
 * Defines methods for managing user subscriptions to topics.
 */
public interface SubscriptionService {
  /**
   * Subscribes the current user to a topic.
   *
   * @param topicId The ID of the topic to subscribe to.
   * @return A confirmation message.
   */
  String subscribe(Long topicId);

  /**
   * Unsubscribes the current user from a topic.
   *
   * @param topicId The ID of the topic to unsubscribe from.
   * @return A confirmation message.
   */
  String unsubscribe(Long topicId);

  /**
   * Retrieves all subscriptions for the current user.
   *
   * @return A list of {@link SubscriptionDto}s representing the user's subscriptions.
   */
  List<SubscriptionDto> getUserSubscriptions();

  /**
   * Checks if the current user is subscribed to a specific topic.
   *
   * @param topicId The ID of the topic to check.
   * @return {@code true} if the user is subscribed, {@code false} otherwise.
   */
  boolean isUserSubscribedToTopic(Long topicId);
}