package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.CreateCommentDto;
import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.mappers.CommentMapper;
import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.models.Post;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for comment-related operations.
 */
@Service
public class CommentServiceImpl implements CommentService {

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CommentMapper commentMapper;

  /**
   * {@inheritDoc}
   */
  @Override
  public CommentDto createComment(@Valid CreateCommentDto createCommentDto) {
    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User currentUser = userRepository.findByEmail(userDetails.getUsername())
        .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userDetails.getUsername()));

    Post targetPost = postRepository.findById(createCommentDto.getPostId())
        .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + createCommentDto.getPostId()));

    Comment newComment = Comment.builder()
        .content(createCommentDto.getContent())
        .user(currentUser)
        .post(targetPost)
        .build();

    Comment savedComment = commentRepository.save(newComment);
    return commentMapper.toDto(savedComment);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CommentDto> getCommentsByPostId(Long postId) {
    if (!postRepository.existsById(postId)) {
      throw new ResourceNotFoundException("Post not found with id: " + postId);
    }
    List<Comment> comments = commentRepository.findByPostId(postId);
    return commentMapper.toDtoList(comments);
  }
}