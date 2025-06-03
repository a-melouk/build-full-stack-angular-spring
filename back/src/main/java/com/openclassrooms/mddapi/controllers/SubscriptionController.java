package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.SubscriptionDto;
import com.openclassrooms.mddapi.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

  @GetMapping("/user")
  public List<SubscriptionDto> getUserSubscriptions() {
    return this.subscriptionService.getUserSubscriptions();
  }

  @GetMapping("/check/{topicId}")
  public Boolean checkSubscription(@PathVariable Long topicId) {
    return this.subscriptionService.isUserSubscribedToTopic(topicId);
  }
}