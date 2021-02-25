package com.pochka15.funfics.domain.user;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "actiity_id")
    @Builder.Default
    private UserActivity activity = new UserActivity();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", isEnabled=" + isEnabled +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                ", activity=" + activity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isEnabled == user.isEnabled && Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(roles, user.roles) && Objects.equals(activity, user.activity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}