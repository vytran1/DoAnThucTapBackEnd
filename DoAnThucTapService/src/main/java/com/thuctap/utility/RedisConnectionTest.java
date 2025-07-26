package com.thuctap.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisConnectionTest implements CommandLineRunner {
	
	 	@Autowired
	    private RedisTemplate<String, String> redisTemplate;

	    @Override
	    public void run(String... args) {
	        redisTemplate.opsForValue().set("testKey", "testValue");
	        String val = redisTemplate.opsForValue().get("testKey");
	        System.out.println("Redis testKey value: " + val);
	    }
	
}
