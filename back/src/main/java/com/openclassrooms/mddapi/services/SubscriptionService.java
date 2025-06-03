package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.SubscriptionDto;
import java.util.List;

public interface SubscriptionService {
  String subscribe(Long topicId);

  String unsubscribe(Long topicId);

  List<SubscriptionDto> getUserSubscriptions();

  boolean isUserSubscribedToTopic(Long topicId);
}