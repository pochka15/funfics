package com.pochka15.funfics.entities.user;

import com.pochka15.funfics.entities.funfic.Comment;
import com.pochka15.funfics.entities.funfic.Funfic;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
@EqualsAndHashCode(of = {"id", "name", "isEnabled", "email"})
@ToString(of = {"id", "name", "isEnabled", "email"})
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String password;

    @Builder.Default
    private boolean isEnabled = true;
    private String email;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<Role> roles = Set.of();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    @Builder.Default
    private UserActivity activity = new UserActivity();

    @OneToMany(mappedBy = "author", orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Funfic> funfics = List.of();

    @OneToMany(mappedBy = "author", orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Comment> comments = List.of();
}