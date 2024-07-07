package com.iwanecki.gamemonitoring.team;

import com.iwanecki.gamemonitoring.user.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "team")
public class TeamDetailedEntity {

    @UuidGenerator
    @Id
    private UUID uuid;

    private String name;

    private Integer maxMembers;

    @Formula("(SELECT COUNT(*) FROM user_ u WHERE u.team_uuid = tde1_0.uuid)")
    private Integer currentMembers;

    @Formula("(SELECT u.username FROM user_ u WHERE u.team_uuid = tde1_0.uuid and u.team_role = 'OWNER')")
    private String owner;

    @Formula("(SELECT sum(u.score) FROM user_ u WHERE u.team_uuid = tde1_0.uuid)")
    private Long totalScore;

    @OneToMany(mappedBy = "team")
    private List<UserEntity> members;
}
