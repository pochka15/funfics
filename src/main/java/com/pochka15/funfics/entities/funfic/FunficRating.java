package com.pochka15.funfics.entities.funfic;

import com.pochka15.funfics.entities.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
public class FunficRating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private float value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funfic_id")
    private Funfic funfic;

    @Override
    public String toString() {
        return "FunficRating{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunficRating that = (FunficRating) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 42;
    }
}
