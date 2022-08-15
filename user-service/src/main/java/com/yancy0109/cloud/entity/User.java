package com.yancy0109.cloud.entity;

import lombok.Data;

/**
 * User实体
 * @author yancy0109
 */
@Data
public class User {
    private Long id;
    private String username;
    private String password;

    public User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
}
