package com.iwanecki.gamemonitoring.user;

import com.iwanecki.gamemonitoring.team.TeamEntity;
import com.iwanecki.gamemonitoring.team.TeamRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "user_")
public class UserEntity {

    @UuidGenerator
    @Id
    private UUID uuid;

    private String username;

    private String password;

    private Integer score;

    @OneToOne
    private TeamEntity team;

    @Enumerated(EnumType.STRING)
    private TeamRole teamRole;
}
