package com.example.tamna.dto;

public class UserDto {
    private int classes;
    private String userId;
    private String userName;


    public UserDto(int classes, String userId, String userName) {
        this.classes = classes;
        this.userId = userId;
        this.userName = userName;
    }

    public int getClasses() {
        return classes;
    }

    public void setClasses(int classes) {
        this.classes = classes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
