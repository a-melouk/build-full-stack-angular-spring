package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.CreatePostDto;
import com.openclassrooms.mddapi.dto.PostDto;
import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.models.Post;
import com.openclassrooms.mddapi.models.Topic;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for post-related operations.
 */
@Service
public class PostServiceImpl implements PostService {

        @Autowired
        private PostRepository postRepository;

        @Autowired
        private TopicRepository topicRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private SubscriptionRepository subscriptionRepository;

        /**
         * {@inheritDoc}
         */
        @Override
        public List<PostDto> getFeed(String sort) {
                // Get authenticated user
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = authentication.getName();
                User currentUser = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));

                // Retrieve topics the user is subscribed to
                List<Long> subscribedTopicIds = subscriptionRepository.findByUserId(currentUser.getId()).stream()
                                .map(subscription -> subscription.getTopic().getId())
                                .toList();

                // If no subscriptions, return empty list early
                if (subscribedTopicIds.isEmpty()) {
                        return java.util.List.of();
                }

                        // Use TopicRepository with EntityGraph to fetch topics and their posts efficiently
        List<Topic> topics = topicRepository.findByIdIn(subscribedTopicIds);

                // Extract all posts from the topics
                List<Post> posts = topics.stream()
                                .flatMap(topic -> topic.getPosts().stream())
                                .toList();

                Comparator<Post> comparator = Comparator.comparing(Post::getCreatedAt);
                if ("desc".equalsIgnoreCase(sort)) {
                        comparator = comparator.reversed();
                }
                return posts.stream()
                                .sorted(comparator)
                                .map(this::toDto)
                                .collect(Collectors.toList());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public PostDto getById(Long id) {
                Post post = postRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
                return toDto(post);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public PostDto create(CreatePostDto createPostDto) {
                Topic topic = topicRepository.findById(createPostDto.getTopicId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Topic not found with id: " + createPostDto.getTopicId()));
                // Get the authenticated user as author
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = authentication.getName();
                User author = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
                Post post = Post.builder()
                                .title(createPostDto.getTitle())
                                .content(createPostDto.getContent())
                                .topic(topic)
                                .user(author)
                                .build();
                Post saved = postRepository.save(post);
                return toDto(saved);
        }

        /**
         * Converts a {@link Post} entity to a {@link PostDto}.
         *
         * @param post The post entity to convert.
         * @return The corresponding DTO.
         */
        private PostDto toDto(Post post) {
                return PostDto.builder()
                                .id(post.getId())
                                .title(post.getTitle())
                                .content(post.getContent())
                                .createdAt(post.getCreatedAt())
                                .username(post.getUser() != null ? post.getUser().getUsernameField() : null)
                                .topicId(post.getTopic().getId())
                                .topicName(post.getTopic().getName())
                                .comments(post.getComments() != null
                                                ? post.getComments().stream().map(this::toCommentDto)
                                                                .collect(Collectors.toList())
                                                : List.of())
                                .build();
        }

        /**
         * Converts a {@link Comment} entity to a {@link CommentDto}.
         *
         * @param comment The comment entity to convert.
         * @return The corresponding DTO.
         */
        private CommentDto toCommentDto(Comment comment) {
                return CommentDto.builder()
                                .id(comment.getId())
                                .content(comment.getContent())
                                .createdAt(comment.getCreatedAt())
                                .build();
        }
}