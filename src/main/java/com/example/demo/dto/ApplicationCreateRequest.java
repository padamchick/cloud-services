package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@Getter @Setter
public class ApplicationCreateRequest {
    @NotBlank(message = "Name field is required")
    private String name;
    @NotBlank(message = "Content field is required")
    private String content;
}
