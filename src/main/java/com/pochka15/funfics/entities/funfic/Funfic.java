package com.pochka15.funfics.entities.funfic;


import com.pochka15.funfics.entities.user.User;
import lombok.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import javax.persistence.*;
import java.util.List;
import java.util.Set;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
@Indexed
@EqualsAndHashCode(of = {"id", "genre", "tags", "name", "description"})
@ToString(of = {"id", "genre", "tags", "name", "description"})
@Table(name = "funfics")
public class Funfic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @ElementCollection(fetch = FetchType.LAZY)
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

    @OneToMany(mappedBy = "funfic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Comment> comments = List.of();

    @OneToMany(mappedBy = "funfic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<FunficRating> ratings = List.of();
}
