package com.pochka15.funfics.domain.funfic;


import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
@ToString
public class Funfic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @ElementCollection
    @Builder.Default
    private Set<String> tags = Set.of();

    private String name;
    private String description;
    private float rating;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Funfic funfic = (Funfic) o;
        return Objects.equals(id, funfic.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
