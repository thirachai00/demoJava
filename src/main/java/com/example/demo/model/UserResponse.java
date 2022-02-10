package com.example.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash(value = "UserResponse", timeToLive = 600)// TTL = 10 Min
public class UserResponse {
    private String name;
    private String gender;
    private String address;
    private String email;

    @Id
    private String seed;
}
