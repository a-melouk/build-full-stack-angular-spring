package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.SubscriptionDto;
import com.openclassrooms.mddapi.exception.BadRequestException;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Topic;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for subscription-related operations.
 */
@Service
public class SubscriptionServiceImpl implements SubscriptionService {

  @Autowired
  private SubscriptionRepository subscriptionRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TopicRepository topicRepository;

  /**
   * {@inheritDoc}
   */
  @Override
  public String subscribe(Long topicId) {
    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = userRepository.findByEmail(userDetails.getUsername())
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    Topic topic = topicRepository.findById(topicId)
        .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + topicId));

    if (subscriptionRepository.existsByUserIdAndTopicId(user.getId(), topic.getId())) {
      throw new BadRequestException("User already subscribed to this topic");
    }

    Subscription subscription = Subscription.builder()
        .user(user)
        .topic(topic)
        .build();
    subscriptionRepository.save(subscription);
    return "Successfully subscribed to " + topic.getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String unsubscribe(Long topicId) {
    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = userRepository.findByEmail(userDetails.getUsername())
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    Topic topic = topicRepository.findById(topicId)
        .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + topicId));

    Subscription subscription = subscriptionRepository.findByUserIdAndTopicId(user.getId(), topic.getId())
        .orElseThrow(() -> new ResourceNotFoundException("Subscription not found for this user and topic"));

    subscriptionRepository.delete(subscription);
    return "Successfully unsubscribed from " + topic.getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SubscriptionDto> getUserSubscriptions() {
    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = userRepository.findByEmail(userDetails.getUsername())
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    List<Subscription> subscriptions = subscriptionRepository.findByUserId(user.getId());

    return subscriptions.stream()
        .map(subscription -> SubscriptionDto.builder()
            .id(subscription.getId())
            .userId(subscription.getUser().getId())
            .topicId(subscription.getTopic().getId())
            .topicName(subscription.getTopic().getName())
            .topicDescription(subscription.getTopic().getDescription())
            .subscribedAt(subscription.getSubscribedAt())
            .build())
        .collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isUserSubscribedToTopic(Long topicId) {
    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = userRepository.findByEmail(userDetails.getUsername())
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    return subscriptionRepository.existsByUserIdAndTopicId(user.getId(), topicId);
  }
}