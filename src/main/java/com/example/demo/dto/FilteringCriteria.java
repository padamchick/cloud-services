package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilteringCriteria {
    private String name;
    private ApplicationState state;

    private int page = 1;
    private int size = 10;

    public FilteringCriteria(String name, ApplicationState state) {
        this.name = name;
        this.state = state;
    }
}
