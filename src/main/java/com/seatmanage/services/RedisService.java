package com.seatmanage.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seatmanage.dto.request.DiagramDraft;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private final Integer TIMEOUT = 10;
    private final RedisTemplate<String,Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisService(RedisTemplate<String,Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void setValue(String key, Object value) {
        System.out.println("key: " + key);
        System.out.println("value: " + value);
        redisTemplate.opsForValue().set(key, value,TIMEOUT, TimeUnit.MINUTES);
    }
    public Object getValueByKey(String key) {
        Object object =  redisTemplate.opsForValue().get(key);
        System.out.println("key: " + key + " value: " + object);
        return object;
    }

    public void deleteValueByKey(String key) {
        System.out.println("deleteValueByKey" + key);
         redisTemplate.delete(key);
    }
    public List<Object> getAllDiagrams() {
        List< Object> diagrams = new ArrayList<>();

        Set<String> keys = redisTemplate.keys("*");
        System.out.println("check keys" + keys);

        for (String key : keys) {
            Object diagram = objectMapper.convertValue(redisTemplate.opsForValue().get(key), DiagramDraft.class);
            System.out.println("check diagram" + diagram);
                    diagrams.add(diagram);
            }
                return diagrams;
    }


}
