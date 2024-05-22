package com.bytebard.sharespace.dtos.auth;

import com.bytebard.sharespace.dtos.post.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String username;
    private String imageUrl;
    private String bio;
    private List<PostDTO> posts;
    private List<PostDTO> likedPosts;
    private List<PostDTO> savedPosts;
    private List<UserDTO> followers;
    private List<UserDTO> following;

    public UserDTO(Long id, String name, String email, String username, String imageUrl, String bio) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.imageUrl = imageUrl;
        this.bio = bio;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", bio='" + bio + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
