package com.example.tamna.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
public class PostBookingDataDto {
    private int classes;
    private String roomId;
    private String userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;
    private List<String> teamMate;


    public PostBookingDataDto(int classes, String roomId, String userId, String startTime, String endTime, List<String> teamMate) {
        this.classes = classes;
        this.roomId = roomId;
        this.userId = userId;
        String[] startTimeParsing = startTime.split(":");
        String[] endTimeParsing = endTime.split(":");
        this.startTime = LocalTime.of(Integer.parseInt(startTimeParsing[0]), Integer.parseInt(startTimeParsing[1]));
        this.endTime = LocalTime.of(Integer.parseInt(endTimeParsing[0]), Integer.parseInt(endTimeParsing[1]));
        this.teamMate = teamMate;
    }
}
