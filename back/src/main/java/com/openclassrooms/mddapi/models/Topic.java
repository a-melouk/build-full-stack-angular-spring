package com.openclassrooms.mddapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a topic entity in the database.
 * Users can subscribe to topics and create posts within them.
 * Topic names are unique.
 */
@Entity
@Table(name = "TOPICS", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topic {
    /**
     * The unique identifier for the topic.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the topic. Must be unique, not blank, and have a maximum size of 50 characters.
     */
    @NotBlank
    @Size(max = 50)
    private String name;

    /**
     * A description of the topic. Maximum size of 255 characters.
     */
    @Size(max = 255)
    private String description;

    /**
     * The list of posts associated with this topic.
     */
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    /**
     * The list of subscriptions to this topic.
     */
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Subscription> subscriptions = new ArrayList<>();
}