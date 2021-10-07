package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FilteringCriteria {
    private String name;
    private ApplicationState state;

    private int page = 1;
    private int size = 10;
}
