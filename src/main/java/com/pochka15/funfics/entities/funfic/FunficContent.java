package com.pochka15.funfics.entities.funfic;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "funfic_contents")
public class FunficContent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String data;

    public FunficContent(String data) {
        this.data = data;
    }
}
