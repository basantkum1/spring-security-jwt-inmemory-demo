package com.jwt.demo.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String username;
    private String email;
    // password is hashed in database
    private String password;
    private String userType;
}
