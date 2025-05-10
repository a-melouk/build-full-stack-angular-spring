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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

        @Autowired
        private PostRepository postRepository;

        @Autowired
        private TopicRepository topicRepository;

        @Autowired
        private UserRepository userRepository;

        @Override
        public List<PostDto> getFeed(String sort) {
                List<Post> posts = postRepository.findAll();
                Comparator<Post> comparator = Comparator.comparing(Post::getCreatedAt);
                if ("desc".equalsIgnoreCase(sort)) {
                        comparator = comparator.reversed();
                }
                return posts.stream()
                                .sorted(comparator)
                                .map(this::toDto)
                                .collect(Collectors.toList());
        }

        @Override
        public PostDto getById(Long id) {
                Post post = postRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
                return toDto(post);
        }

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

        private PostDto toDto(Post post) {
                return PostDto.builder()
                                .id(post.getId())
                                .title(post.getTitle())
                                .content(post.getContent())
                                .createdAt(post.getCreatedAt())
                                .authorName(post.getUser().getFirstName() + " " + post.getUser().getLastName())
                                .topicId(post.getTopic().getId())
                                .topicName(post.getTopic().getName())
                                .comments(post.getComments() != null
                                                ? post.getComments().stream().map(this::toCommentDto)
                                                                .collect(Collectors.toList())
                                                : List.of())
                                .build();
        }

        private CommentDto toCommentDto(Comment comment) {
                return CommentDto.builder()
                                .id(comment.getId())
                                .content(comment.getContent())
                                .createdAt(comment.getCreatedAt())
                                .authorName(comment.getUser().getFirstName() + " " + comment.getUser().getLastName())
                                .build();
        }
}