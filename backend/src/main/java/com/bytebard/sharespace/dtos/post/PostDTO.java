package com.bytebard.sharespace.dtos.post;

import com.bytebard.sharespace.dtos.auth.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDTO {

    private long id;

    private String caption;

    private String imageUrl;

    private String imageId;

    private String location;

    private List<String> tags;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;

    private UserDTO creator;

    private List<UserDTO> likes;

    private List<UserDTO> saves;

    public PostDTO(long id, String caption, String imageUrl, String imageId, String location, List<String> tags, LocalDateTime createdAt, UserDTO creator) {
        this.id = id;
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.imageId = imageId;
        this.location = location;
        this.tags = tags;
        this.createdAt = createdAt;
        this.creator = creator;
    }
}
