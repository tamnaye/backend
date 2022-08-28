package com.example.tamna.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    private int floor;
    private int roomId;
    private String roomName;
    private String roomType;
}
