package com.pochka15.funfics.domain.funfic;


import com.pochka15.funfics.domain.user.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
@ToString
@Indexed
public class Funfic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @ElementCollection
    @Builder.Default
    private Set<String> tags = Set.of();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "funfic_id", referencedColumnName = "id")
    private FunficContent funficContent;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @FullTextField
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
