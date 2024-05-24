package com.bytebard.sharespace.controllers;

import com.bytebard.sharespace.dtos.ApiResponse;
import com.bytebard.sharespace.dtos.post.PostDTO;
import com.bytebard.sharespace.dtos.post.PostDTORequest;
import com.bytebard.sharespace.exceptions.NotFoundException;
import com.bytebard.sharespace.services.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @RequestParam(value = "searchValue", defaultValue = "", required = false) String searchValue,
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "perPage", defaultValue = "20", required = false) int perPage,
            @RequestParam(value = "userId", required = false) Integer userId
    ) throws IOException {
        List<PostDTO> posts = postService.getAllPosts(searchValue, userId, page, perPage);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), null, posts), HttpStatus.OK);
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<PostDTO>>> getRecentPosts() throws IOException {
        List<PostDTO> posts = postService.getRecentPosts();
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), null, posts), HttpStatus.OK);
    }

    @PostMapping(value = "update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<PostDTO>> updatePost(
            @PathVariable("id") Long postId,
            @Valid @RequestPart("post") PostDTORequest createPostDTO,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException, NotFoundException {
        PostDTO postDTO = postService.updatePost(postId, createPostDTO, file);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), null, postDTO), HttpStatus.OK);
    }

    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<ApiResponse<PostDTO>> deletePost(
            @PathVariable("id") Long postId
    ) throws IOException, NotFoundException {
        postService.deletePostById(postId);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "Deleted successfully", null), HttpStatus.OK);
    }

    @GetMapping("/saved")
    public ResponseEntity<ApiResponse<List<PostDTO>>> getSavedPosts() {
        List<PostDTO> posts = postService.getSavedPosts();
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), null, posts), HttpStatus.OK);
    }

    @GetMapping("/liked")
    public ResponseEntity<ApiResponse<List<PostDTO>>> getLikedPosts() {
        List<PostDTO> posts = postService.getLikedPosts();
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), null, posts), HttpStatus.OK);
    }

    @PostMapping("/like/{id}")
    public ResponseEntity<ApiResponse<PostDTO>> likePost(@PathVariable("id") Long postId) throws NotFoundException {
        PostDTO postDTO = postService.likePost(postId);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), null, postDTO), HttpStatus.OK);
    }

    @PostMapping("/save/{id}")
    public ResponseEntity<ApiResponse<PostDTO>> savePost(@PathVariable("id") Long postId) throws NotFoundException {
        PostDTO postDTO = postService.savePost(postId);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), null, postDTO), HttpStatus.OK);
    }

    @DeleteMapping("/save/{id}")
    public ResponseEntity<ApiResponse<PostDTO>> deleteSavedPost(@PathVariable("id") Long postId) throws NotFoundException {
        postService.deleteSavedPost(postId);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), null, null), HttpStatus.OK);
    }

    @DeleteMapping("/like/{id}")
    public ResponseEntity<ApiResponse<PostDTO>> deleteLikedPost(@PathVariable("id") Long postId) throws NotFoundException {
        postService.deleteLikedPost(postId);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), null, null), HttpStatus.OK);
    }
}
