package com.example.tenpo.repository;


import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class RedisRepository {

    private final StringRedisTemplate stringRedisTemplate;

    @Value("${redis.default.ttl}")
    private long defaultRedisTtl;

    public RedisRepository(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void saveIntoRedis(String key, int value){
        System.out.print("Saved in Redis: "+key+" value: "+value);
        stringRedisTemplate.opsForValue().set(key, String.valueOf(value), defaultRedisTtl, TimeUnit.SECONDS);
    }

    public int getIntFromRedis(String key){
        System.out.print("Looking into Redis the Key: "+ key);
        String value = stringRedisTemplate.opsForValue().get(key);
        if(StringUtils.isNotEmpty(value)){
            return Integer.parseInt(value);
        }
        throw new RuntimeException("Value Not found In REDIS after 3 attempt in external service.");
    }
}
