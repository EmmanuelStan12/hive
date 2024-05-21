package com.bytebard.sharespace.dtos.post;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDTORequest {

    @NotEmpty(message = "Caption cannot be empty")
    private String caption;

    private String location;

    private String tags;
}
