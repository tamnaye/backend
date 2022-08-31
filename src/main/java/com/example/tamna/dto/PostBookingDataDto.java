package com.example.tamna.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostBookingDataDto {
    private int classes;
    private int roomId;
    private String roomType;
    private String userId;
    private String userName;
    private String startTime;
    private String endTime;
    private List<String> teamMate;
//    private boolean official;
}
