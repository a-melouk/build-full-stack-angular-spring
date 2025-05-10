package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

  @Autowired
  private SubscriptionService subscriptionService;

  @PostMapping("/subscribe/{topicId}")
  public String subscribe(@PathVariable Long topicId) {
    return this.subscriptionService.subscribe(topicId);
  }

  @DeleteMapping("/unsubscribe/{topicId}")
  public String unsubscribe(@PathVariable Long topicId) {
    return this.subscriptionService.unsubscribe(topicId);
  }
}