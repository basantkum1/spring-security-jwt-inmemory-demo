package com.jwt.demo.repository;

import com.jwt.demo.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserInMemRepository {
    // ConcurrentHashMap - as it's thread-safe map for in-memory storage
    private final ConcurrentHashMap<String, User> userTempStore = new ConcurrentHashMap<>();

    public void save(User user) {
        userTempStore.put(user.getUsername(), user);
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userTempStore.get(username));
    }
}