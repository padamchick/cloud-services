package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class ApplicationUpgradeRequest {
    @NotNull(message = "id field is required")
    private Long id;
    @NotNull(message = "state field is required")
    private ApplicationState state;
    private String comment;
}
