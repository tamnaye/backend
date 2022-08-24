package com.example.tamna.config.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ADMIN("ROLE_USER, ROLE_ADMIN"),
    USER("ROLE_USER");

    private String value;
}
