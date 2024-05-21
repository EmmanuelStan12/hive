package com.bytebard.sharespace.controllers;

import com.bytebard.sharespace.config.security.JwtAuthenticationToken;
import com.bytebard.sharespace.dtos.ApiResponse;
import com.bytebard.sharespace.dtos.post.PostDTO;
import com.bytebard.sharespace.dtos.post.PostDTORequest;
import com.bytebard.sharespace.exceptions.NotFoundException;
import com.bytebard.sharespace.models.User;
import com.bytebard.sharespace.services.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/posts")
@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(value = "create", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<PostDTO>> createPost(@Valid @RequestPart("post") PostDTORequest createPostDTO, @RequestPart("file") MultipartFile file) throws IOException {
        PostDTO postDTO = postService.createPost(createPostDTO, file);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), null, postDTO), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDTO>> getPostById(@PathVariable("id") Long postId) throws NotFoundException {
        PostDTO postDTO = postService.getPostById(postId);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), null, postDTO), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostDTO>>> getAllPosts(
            @RequestParam(value = "searchValue", defaultValue = "") String searchValue,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "perPage", defaultValue = "20") int perPage
    ) throws IOException {
        List<PostDTO> posts = postService.getAllPosts(searchValue, page, perPage);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), null, posts), HttpStatus.OK);
    }

    @PostMapping(value = "update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<PostDTO>> updatePost(
            @PathVariable("id") Long postId,
            @Valid @RequestPart("post") PostDTORequest createPostDTO,
            @RequestPart("file") MultipartFile file
    ) throws IOException, NotFoundException {
        PostDTO postDTO = postService.updatePost(postId, createPostDTO, file);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), null, postDTO), HttpStatus.OK);
    }

    @DeleteMapping(value = "delete/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<PostDTO>> deletePost(
            @PathVariable("id") Long postId
    ) throws IOException, NotFoundException {
        postService.deletePostById(postId);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "Deleted successfully", null), HttpStatus.OK);
    }
}
