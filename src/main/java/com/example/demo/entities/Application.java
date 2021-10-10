package com.example.demo.entities;

import com.example.demo.dto.ApplicationState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Audited
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String content;
    @Enumerated(EnumType.STRING)
    private ApplicationState state;

    private String comment;
    private Long bid;

    public Application(String name, String content) {
        this.name = name;
        this.content = content;
    }
}
