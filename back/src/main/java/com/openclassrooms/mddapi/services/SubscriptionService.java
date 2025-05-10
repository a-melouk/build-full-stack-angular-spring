package com.openclassrooms.mddapi.services;

public interface SubscriptionService {
  String subscribe(Long topicId);

  String unsubscribe(Long topicId);
}