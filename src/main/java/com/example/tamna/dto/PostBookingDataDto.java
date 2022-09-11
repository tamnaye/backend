package com.example.tamna.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostBookingDataDto {
    private int roomId;
    private String roomType;
    private String startTime;
    private String endTime;
    private List<String> teamMate;
}
