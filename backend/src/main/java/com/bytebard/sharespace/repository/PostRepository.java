package com.bytebard.sharespace.repository;

import com.bytebard.sharespace.models.Post;
import com.bytebard.sharespace.models.User;
import javafx.geometry.Pos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p")
    List<Post> findAll(@Param("searchValue") String searchValue, Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.saves s WHERE s.id = :userId")
    List<Post> findSavedPosts(@Param("userId") Long userId);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.likes l WHERE l.id = :userId")
    List<Post> findLikedPosts(@Param("userId") Long userId);
}
