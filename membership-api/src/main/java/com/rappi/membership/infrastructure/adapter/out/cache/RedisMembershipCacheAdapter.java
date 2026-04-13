package com.rappi.membership.infrastructure.adapter.out.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rappi.membership.domain.model.Membership;
import com.rappi.membership.domain.port.out.MembershipCachePort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
public class RedisMembershipCacheAdapter implements MembershipCachePort {

    private static final String CACHE_PREFIX = "membership:";
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisMembershipCacheAdapter(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void save(Membership membership) {
        try {
            String val = objectMapper.writeValueAsString(membership);
            redisTemplate.opsForValue().set(CACHE_PREFIX + membership.userId(), val, Duration.ofMinutes(10));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error seralizing membership for redis", e);
        }
    }

    @Override
    public Optional<Membership> get(String userId) {
        String val = redisTemplate.opsForValue().get(CACHE_PREFIX + userId);
        if (val != null) {
            try {
                return Optional.of(objectMapper.readValue(val, Membership.class));
            } catch (JsonProcessingException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
}
