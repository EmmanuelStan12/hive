package com.bytebard.sharespace.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String username;
    private String bio;
    private String imageUrl;
    private String imageId;

    @ManyToMany(mappedBy = "following")
    private List<User> followers;

    @ManyToMany
    @JoinTable(name = "user_following", // Join table for followers
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id"))
    private List<User> following;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @ManyToMany(mappedBy = "likes") // ManyToMany with likedBy field in Post
    private List<Post> likedPosts = new ArrayList<>();

    @ManyToMany(mappedBy = "saves") // ManyToMany with savedBy field in Post
    private List<Post> savedPosts = new ArrayList<>();

    public User(Long id, String name, String email, String password, String username, String bio, String imageUrl, String imageId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.username = username;
        this.bio = bio;
        this.imageUrl = imageUrl;
        this.imageId = imageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
