package com.iwanecki.gamemonitoring;

import com.iwanecki.gamemonitoring.user.UserEntity;
import com.iwanecki.gamemonitoring.user.UserRankRepository;
import com.iwanecki.gamemonitoring.user.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.List;

@SpringBootApplication
public class GameMonitoringApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(GameMonitoringApplication.class, args);

        UserRankRepository userRankRepository = context.getBean(UserRankRepository.class);
        UserRepository userRepository = context.getBean(UserRepository.class);

        List<UserEntity> users = userRepository.findAll();
        users.forEach(user -> userRankRepository.addUserScore(user.getUuid(), user.getScore()));
    }

    @Bean
    @SuppressWarnings("unchecked")
    public DefaultRedisScript<List<Long>> redisscript() {
        DefaultRedisScript<List<Long>> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("script.lua")));

        defaultRedisScript.setResultType((Class<List<Long>>)(Object)List.class);
        return defaultRedisScript;
    }

}
