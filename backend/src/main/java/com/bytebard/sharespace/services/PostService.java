package com.bytebard.sharespace.services;

import com.bytebard.sharespace.dtos.post.PostDTORequest;
import com.bytebard.sharespace.dtos.post.PostDTO;
import com.bytebard.sharespace.exceptions.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {

    PostDTO createPost(PostDTORequest createPostDTO, MultipartFile file) throws IOException;

    PostDTO getPostById(Long id) throws NotFoundException;

    void deletePostById(Long id) throws NotFoundException, IOException;

    PostDTO updatePost(Long id, PostDTORequest updatePostDTO, MultipartFile file) throws IOException, NotFoundException;

    List<PostDTO> getAllPosts(String searchValue, Integer page, Integer perPage);

}