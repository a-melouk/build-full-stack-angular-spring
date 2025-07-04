package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.SubscriptionDto;
import com.openclassrooms.mddapi.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling user subscription operations for topics.
 * This includes subscribing to a topic, unsubscribing from a topic, and listing a user's subscriptions.
 */
@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

  @Autowired
  private SubscriptionService subscriptionService;

  /**
   * Subscribes the current user to a specific topic.
   *
   * @param topicId The ID of the topic to subscribe to.
   * @return A confirmation message.
   */
  @PostMapping("/subscribe/{topicId}")
  public String subscribe(@PathVariable Long topicId) {
    return this.subscriptionService.subscribe(topicId);
  }

  /**
   * Unsubscribes the current user from a specific topic.
   *
   * @param topicId The ID of the topic to unsubscribe from.
   * @return A confirmation message.
   */
  @DeleteMapping("/unsubscribe/{topicId}")
  public String unsubscribe(@PathVariable Long topicId) {
    return this.subscriptionService.unsubscribe(topicId);
  }

  /**
   * Retrieves all topic subscriptions for the current user.
   *
   * @return A list of {@link SubscriptionDto} objects representing the user's subscriptions.
   */
  @GetMapping("/user")
  public List<SubscriptionDto> getUserSubscriptions() {
    return this.subscriptionService.getUserSubscriptions();
  }

  /**
   * Checks if the current user is subscribed to a specific topic.
   *
   * @param topicId The ID of the topic to check.
   * @return A boolean value, true if subscribed, false otherwise.
   */
  @GetMapping("/check/{topicId}")
  public Boolean checkSubscription(@PathVariable Long topicId) {
    return this.subscriptionService.isUserSubscribedToTopic(topicId);
  }
}