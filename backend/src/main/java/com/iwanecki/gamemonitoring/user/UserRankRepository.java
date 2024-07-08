package com.iwanecki.gamemonitoring.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class UserRankRepository {


    private final RedisTemplate<String, String> redisTemplate;
    private final DefaultRedisScript<List<Long>> redisScript;

    private static final String USER_SCORE_KEY = "user:score";

    public void addUserScore(UUID userUuid, Integer score) {
        redisTemplate.opsForZSet().add(USER_SCORE_KEY, userUuid.toString(), score);

    }

    public List<Long> listUserRanks(List<UUID> userUuidList) {
        Object[] userUuidStringList = userUuidList.stream().map(UUID::toString).toArray();
        return redisTemplate.execute(redisScript, List.of(USER_SCORE_KEY), userUuidStringList);
    }
}
