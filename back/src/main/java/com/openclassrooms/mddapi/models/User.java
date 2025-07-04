package com.openclassrooms.mddapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a user entity in the database.
 * This class also implements the {@link UserDetails} interface for Spring Security integration.
 * Email and username are unique.
 */
@Entity
@Table(name = "USERS", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "username")
})
@Data
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = { "id" })
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "posts", "comments", "subscriptions" })
public class User implements UserDetails {
    /**
     * The unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user's email address. It is unique and must be a valid email format.
     */
    @NonNull
    @Size(max = 50)
    @Email
    private String email;

    /**
     * The user's username. It is unique and must be between 3 and 20 characters.
     */
    @NonNull
    @Size(min = 3, max = 20)
    @Column(unique = true)
    private String username;

    /**
     * The user's hashed password.
     */
    @NonNull
    @Size(max = 120)
    private String password;

    /**
     * The timestamp when the user account was created.
     */
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * The timestamp when the user account was last updated.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * The list of posts created by this user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    /**
     * The list of comments made by this user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    /**
     * The list of topics this user is subscribed to.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Subscription> subscriptions = new ArrayList<>();

    /**
     * Returns the authorities granted to the user.
     * In this implementation, users have no specific authorities/roles.
     * @return A collection of granted authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    /**
     * Returns the password used to authenticate the user.
     * @return The user's password.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the username used to authenticate the user. For Spring Security, this is the user's email.
     * @return The user's email address.
     */
    @Override
    public String getUsername() {
        // For Spring Security authentication, we use email as the username
        return email;
    }

    /**
     * Returns the actual username field of the user, as distinct from the email used for authentication.
     * @return The user's username.
     */
    public String getUsernameField() {
        return username;
    }

}