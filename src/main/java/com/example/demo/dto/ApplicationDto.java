package com.example.demo.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationDto {
    private Long id;
    private String name;
    private String content;
    private ApplicationState state;
    private String comment;
    private Long bid;
}
