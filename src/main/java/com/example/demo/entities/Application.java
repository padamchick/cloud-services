package com.example.demo.entities;

import com.example.demo.dto.ApplicationState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String content;
    @Enumerated(EnumType.STRING)
    private ApplicationState state;

}
