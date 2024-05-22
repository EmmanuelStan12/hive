package com.bytebard.sharespace.mappers;

import com.bytebard.sharespace.dtos.auth.CreateUserDTO;
import com.bytebard.sharespace.dtos.auth.UpdateUserDTO;
import com.bytebard.sharespace.dtos.auth.UserDTO;
import com.bytebard.sharespace.dtos.post.PostDTO;
import com.bytebard.sharespace.dtos.post.PostDTORequest;
import com.bytebard.sharespace.models.Post;
import com.bytebard.sharespace.models.User;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class MapperUtils {

    public static PostDTO toPostDTO(Post post, boolean addLikes, boolean addSaves) {
        String tags = post.getTags();
        List<String> tagsList = List.of();
        if (tags != null && !tags.isEmpty()) {
            tagsList = List.of(tags.split(","));
        }
        PostDTO postDTO = new PostDTO(
                post.getId(),
                post.getCaption(),
                post.getImageUrl(),
                post.getImageId(),
                post.getLocation(),
                tagsList,
                post.getCreatedAt(),
                toUserDTO(post.getCreator())
        );

        if (!addLikes && !addSaves) {
            return postDTO;
        }

        if (addLikes && !CollectionUtils.isEmpty(post.getLikes())) {
            List<UserDTO> likedUsers = post.getLikes().stream().map(MapperUtils::toUserDTO).toList();
            postDTO.setLikes(likedUsers);
        }

        if (addSaves && !CollectionUtils.isEmpty(post.getSaves())) {
            List<UserDTO> savedUsers = post.getSaves().stream().map(MapperUtils::toUserDTO).toList();
            postDTO.setSaves(savedUsers);
        }

        return postDTO;
    }

    public static Post toPost(PostDTORequest postDTO) {
        return new Post(
                null,
                postDTO.getCaption(),
                null,
                null,
                postDTO.getLocation(),
                postDTO.getTags(),
                null,
                null,
                null,
                null
        );
    }

    public static UserDTO toUserDTO(User user, boolean addPosts, boolean addFollows) {
        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUsername(),
                user.getImageUrl(),
                user.getBio()
        );
        if (!addPosts && !addFollows) {
            return userDTO;
        }

        if (addPosts && !CollectionUtils.isEmpty(user.getLikedPosts())) {
            List<PostDTO> likedPosts = user.getLikedPosts().stream().map(MapperUtils::toPostDTO).toList();
            userDTO.setLikedPosts(likedPosts);
        }

        if (addPosts && !CollectionUtils.isEmpty(user.getSavedPosts())) {
            List<PostDTO> savedPosts = user.getSavedPosts().stream().map(MapperUtils::toPostDTO).toList();
            userDTO.setSavedPosts(savedPosts);
        }

        if (addPosts && !CollectionUtils.isEmpty(user.getPosts())) {
            List<PostDTO> posts = user.getPosts().stream().map(MapperUtils::toPostDTO).toList();
            userDTO.setPosts(posts);
        }

        if (addFollows && !CollectionUtils.isEmpty(user.getFollowers())) {
            userDTO.setFollowers(user.getFollowers().stream().map(MapperUtils::toUserDTO).toList());
        }

        if (addFollows && !CollectionUtils.isEmpty(user.getFollowing())) {
            userDTO.setFollowers(user.getFollowing().stream().map(MapperUtils::toUserDTO).toList());
        }

        return userDTO;
    }

    public static UserDTO toUserDTO(User user) {
        return toUserDTO(user, false, false);
    }

    public static PostDTO toPostDTO(Post post) {
        return toPostDTO(post, false, false);
    }

    public static User toUser(CreateUserDTO dto) {
        return new User(
                null,
                dto.getName(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getUsername(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}
