package com.bytebard.sharespace.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private List<Post> posts;

    @ManyToMany(mappedBy = "likes") // ManyToMany with likedBy field in Post
    private List<Post> likedPosts;

    @ManyToMany(mappedBy = "saves") // ManyToMany with savedBy field in Post
    private List<Post> savedPosts;
}
