package com.openclassrooms.mddapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Represents a subscription entity in the database.
 * This entity creates a many-to-many relationship between Users and Topics,
 * indicating that a user is subscribed to a topic.
 * A unique constraint ensures a user can only subscribe to a topic once.
 */
@Entity
@Table(name = "SUBSCRIPTIONS", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "topic_id"})
})
@Data
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = {"id"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Subscription {
    /**
     * The unique identifier for the subscription.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user who is subscribed.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    /**
     * The topic to which the user is subscribed.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    @NotNull
    private Topic topic;

    /**
     * The timestamp when the subscription was created. Automatically set.
     */
    @CreatedDate
    @Column(name = "subscribed_at", updatable = false)
    private LocalDateTime subscribedAt;
}