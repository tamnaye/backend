package com.example.tamna;

import com.example.tamna.model.Room;

public enum RoomType {
    MEETING("meeting", "회의실"),
    NABOX("nabox", "나박스"),
    STUDIO("studio", "스튜디오");
    public String lowerCase;
    public String name;

    RoomType(String lowerCase, String name) {
        this.lowerCase = lowerCase;
        this.name = name;
    }

}
