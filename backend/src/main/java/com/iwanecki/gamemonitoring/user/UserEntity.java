package com.iwanecki.gamemonitoring.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

}
