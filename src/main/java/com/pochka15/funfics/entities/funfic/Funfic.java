package com.pochka15.funfics.entities.funfic;


import com.pochka15.funfics.entities.user.User;
import lombok.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    private User author;

    @FullTextField
    private String name;

    private String description;

    @OneToMany(mappedBy = "funfic", orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Comment> comments = List.of();

    @OneToMany(mappedBy = "funfic", orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<FunficRating> ratings = List.of();

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

    @Override
    public String toString() {
        return "Funfic{" +
                "id=" + id +
                ", genre=" + genre +
                ", tags=" + tags +
                ", name='" + name + '\'' +
                ", description='" + description +
                '}';
    }
}
