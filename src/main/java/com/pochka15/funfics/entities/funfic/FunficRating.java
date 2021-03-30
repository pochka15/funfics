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
@EqualsAndHashCode(of = {"id", "value"})
@ToString(of = {"id", "value"})
@Table(name = "funfic_rating")
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
}
