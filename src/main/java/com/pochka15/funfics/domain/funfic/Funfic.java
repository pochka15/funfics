package com.pochka15.funfics.domain.funfic;


import com.pochka15.funfics.domain.user.User;
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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "funfic_id", referencedColumnName = "id")
    private FunficContent funficContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    private User author;

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
