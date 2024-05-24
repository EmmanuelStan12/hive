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
import com.bytebard.sharespace.services.UserService;
import javafx.util.Pair;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final FileUploadHelper fileUploadHelper;
    private final UserService userService;

    public PostServiceImpl(PostRepository postRepository, FileUploadHelper fileUploadHelper, UserService userService) {
        this.postRepository = postRepository;
        this.fileUploadHelper = fileUploadHelper;
        this.userService = userService;
    }

    @Override
    public PostDTO createPost(PostDTORequest createPostDTO, MultipartFile file) throws IOException {
        Pair<String, String> fileProps = fileUploadHelper.upload(file);

        Post post = MapperUtils.toPost(createPostDTO);

        post.setImageId(fileProps.getKey());
        post.setImageUrl(fileProps.getValue());

        post.setCreator(userService.getCurrentUser());

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

        post.setCaption(updatePostDTO.getCaption());
        post.setTags(updatePostDTO.getTags());
        post.setLocation(updatePostDTO.getLocation());

        if (file != null) {
            fileUploadHelper.delete(post.getImageId());
            Pair<String, String> fileProps = fileUploadHelper.upload(file);

            post.setImageId(fileProps.getKey());
            post.setImageUrl(fileProps.getValue());

        }
        post = postRepository.save(post);
        return MapperUtils.toPostDTO(post);
    }

    @Override
    public List<PostDTO> getAllPosts(String searchValue, Integer userId, Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by("createdAt"));
        List<Post> posts;
        if (userId != null) {
            posts = postRepository.findAllByUserId(searchValue, userId, pageable);
        } else {
            posts = postRepository.findAll(searchValue, pageable);
        }

        return posts.stream().map(post -> MapperUtils.toPostDTO(post, true, true)).toList();
    }

    @Override
    public List<PostDTO> getRecentPosts() {
        List<Post> posts = postRepository.findPopularPosts();
        if (CollectionUtils.isEmpty(posts)) {
            posts = postRepository.findAll("", PageRequest.of(0, 20));
        }

        return posts.stream().map(post -> MapperUtils.toPostDTO(post, true, true)).toList();
    }

    @Override
    public List<PostDTO> getLikedPosts() {
        List<Post> posts = postRepository.findLikedPosts(userService.getCurrentUser().getId());

        return posts.stream().map(MapperUtils::toPostDTO).toList();
    }

    @Override
    public List<PostDTO> getSavedPosts() {
        List<Post> posts = postRepository.findSavedPosts(userService.getCurrentUser().getId());

        return posts.stream().map(MapperUtils::toPostDTO).toList();
    }

    @Override
    public void deleteSavedPost(Long postId) throws NotFoundException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));

        if (!post.getSaves().contains(userService.getCurrentUser())) {
            throw new IllegalArgumentException("You are not allowed to remove this saved post");
        }

        post.getSaves().remove(userService.getCurrentUser());
        postRepository.save(post);
    }

    @Override
    public void deleteLikedPost(Long postId) throws NotFoundException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));

        if (!post.getLikes().contains(userService.getCurrentUser())) {
            throw new IllegalArgumentException("You are not allowed to unlike the post");
        }

        post.getLikes().remove(userService.getCurrentUser());
        postRepository.save(post);
    }

    @Override
    public PostDTO likePost(Long postId) throws NotFoundException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));

        post.getLikes().add(userService.getCurrentUser());
        post = postRepository.save(post);

        return MapperUtils.toPostDTO(post, true, true);
    }

    @Override
    public PostDTO savePost(Long postId) throws NotFoundException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));

        post.getSaves().add(userService.getCurrentUser());
        post = postRepository.save(post);

        return MapperUtils.toPostDTO(post, true, true);
    }
}
