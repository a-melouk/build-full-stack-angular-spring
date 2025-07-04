package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link Subscription} entities.
 * Provides standard CRUD operations and custom query methods for accessing subscription data.
 */
@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    /**
     * Finds all subscriptions for a specific user.
     *
     * @param userId the ID of the user.
     * @return a list of subscriptions for the given user.
     */
    List<Subscription> findByUserId(Long userId);

    /**
     * Finds a subscription for a specific user and topic.
     *
     * @param userId the ID of the user.
     * @param topicId the ID of the topic.
     * @return an {@link Optional} containing the subscription if it exists, otherwise empty.
     */
    Optional<Subscription> findByUserIdAndTopicId(Long userId, Long topicId);

    /**
     * Checks if a subscription exists for a specific user and topic.
     *
     * @param userId the ID of the user.
     * @param topicId the ID of the topic.
     * @return {@code true} if a subscription exists, {@code false} otherwise.
     */
    boolean existsByUserIdAndTopicId(Long userId, Long topicId);

}