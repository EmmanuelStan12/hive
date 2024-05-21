package com.bytebard.sharespace.mappers;

import com.bytebard.sharespace.dtos.post.PostDTORequest;
import com.bytebard.sharespace.dtos.post.PostDTO;
import com.bytebard.sharespace.models.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper implements Mapper<Post, PostDTO> {

    private final UserMapper userMapper;

    public PostMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public PostDTO convertToDTO(Post post) {
        return new PostDTO(
                post.getId(),
                post.getCaption(),
                post.getImageUrl(),
                post.getImageId(),
                post.getLocation(),
                post.getTags(),
                post.getCreatedAt(),
                userMapper.convertToDTO(post.getCreator())
        );
    }

    @Override
    public Post convertToModel(PostDTO postDTO) {
        return null;
    }

    public Post convertToModel(PostDTORequest createPostDTO) {
        return new Post(
                null,
                createPostDTO.getCaption(),
                null,
                null,
                createPostDTO.getLocation(),
                createPostDTO.getTags(),
                null,
                null
        );
    }
}
