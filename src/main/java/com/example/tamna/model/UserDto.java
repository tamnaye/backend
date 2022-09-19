package com.example.tamna.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int classes;
    private String userId;
    private String userName;
    private String roles;
    private int floor;
}