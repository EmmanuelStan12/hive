package com.bytebard.sharespace.dtos.post;

import com.bytebard.sharespace.dtos.auth.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    private String tags;

    private LocalDateTime createdAt;

    private UserDTO creator;
}
