package com.openclassrooms.mddapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a post entity in the database.
 * Each post has a title, content, an author (user), and belongs to a topic.
 * It can also have multiple comments.
 */
@Entity
@Table(name = "POSTS")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"})
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)

@ToString(exclude = {"comments"})
public class Post {
    /**
     * The unique identifier for the post.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The title of the post. Cannot be blank and has a maximum size of 100 characters.
     */
    @NonNull
    @NotBlank
    @Size(max = 100)
    private String title;

    /**
     * The content of the post. Cannot be blank.
     */
    @NonNull
    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * The timestamp when the post was created. Automatically set.
     */
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    /**
     * The user who created the post.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    /**
     * The topic to which the post belongs.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    @NotNull
    private Topic topic;

    /**
     * The list of comments associated with this post.
     */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();
}