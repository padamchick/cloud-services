package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class ApplicationUpdateContentRequest {
//    assumption: the name once set cannot be changed
    @NotNull(message = "id field is required")
    private Long id;
    @NotNull(message = "content field is required")
    private String content;
}
