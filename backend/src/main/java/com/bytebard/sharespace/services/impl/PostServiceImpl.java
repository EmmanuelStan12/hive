package com.bytebard.sharespace.services.impl;

import com.bytebard.sharespace.config.security.JwtAuthenticationToken;
import com.bytebard.sharespace.dtos.post.PostDTORequest;
import com.bytebard.sharespace.dtos.post.PostDTO;
import com.bytebard.sharespace.exceptions.NotFoundException;
import com.bytebard.sharespace.files.FileUploadHelper;
import com.bytebard.sharespace.mappers.MapperUtils;
import com.bytebard.sharespace.models.Post;
import com.bytebard.sharespace.models.User;
import com.bytebard.sharespace.repository.PostRepository;
import com.bytebard.sharespace.services.PostService;
import javafx.util.Pair;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final FileUploadHelper fileUploadHelper;

    public PostServiceImpl(PostRepository postRepository, FileUploadHelper fileUploadHelper) {
        this.postRepository = postRepository;
        this.fileUploadHelper = fileUploadHelper;
    }

    @Override
    public PostDTO createPost(PostDTORequest createPostDTO, MultipartFile file) throws IOException {
        Pair<String, String> fileProps = fileUploadHelper.upload(file);

        Post post = MapperUtils.toPost(createPostDTO);

        post.setImageId(fileProps.getKey());
        post.setImageUrl(fileProps.getValue());

        post.setCreator(currentUser());

        post = postRepository.save(post);
        return MapperUtils.toPostDTO(post, true, true);
    }

    @Override
    public PostDTO getPostById(Long id) throws NotFoundException {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found"));
        return MapperUtils.toPostDTO(post, true, true);
    }

    @Override
    public void deletePostById(Long id) throws NotFoundException, IOException {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found"));
        if (post.getImageId() != null) {
            fileUploadHelper.delete(post.getImageId());
        }
        postRepository.delete(post);
    }

    @Override
    public PostDTO updatePost(Long id, PostDTORequest updatePostDTO, MultipartFile file) throws IOException, NotFoundException {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found"));

        Post updatedPost = MapperUtils.toPost(updatePostDTO);
        updatedPost.setId(post.getId());

        if (file != null) {
            fileUploadHelper.delete(post.getImageId());
            Pair<String, String> fileProps = fileUploadHelper.upload(file);

            updatedPost.setImageId(fileProps.getKey());
            updatedPost.setImageUrl(fileProps.getValue());

            updatedPost = postRepository.save(post);
        }
        return MapperUtils.toPostDTO(updatedPost);
    }

    @Override
    public List<PostDTO> getAllPosts(String searchValue, Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by("createdAt"));
        List<Post> posts = postRepository.findAll(searchValue, pageable);

        return posts.stream().map(post -> MapperUtils.toPostDTO(post, true, true)).toList();
    }

    @Override
    public List<PostDTO> getLikedPosts() {
        List<Post> posts = postRepository.findSavedPosts(currentUser().getId());

        return posts.stream().map(MapperUtils::toPostDTO).toList();
    }

    @Override
    public List<PostDTO> getSavedPosts() {
        List<Post> posts = postRepository.findLikedPosts(currentUser().getId());

        return posts.stream().map(MapperUtils::toPostDTO).toList();
    }

    private User currentUser() {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
