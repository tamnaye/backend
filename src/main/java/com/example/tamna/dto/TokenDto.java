package com.example.tamna.dto;

import lombok.Data;

@Data
public class TokenDto {
    private String userId;
    private String refreshToken;

    public TokenDto(String userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }

}
