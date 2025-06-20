package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUserId(Long userId);
    List<Subscription> findByTopicId(Long topicId);
    Optional<Subscription> findByUserIdAndTopicId(Long userId, Long topicId);
    boolean existsByUserIdAndTopicId(Long userId, Long topicId);

}