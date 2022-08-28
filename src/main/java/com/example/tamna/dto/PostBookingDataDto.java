package com.example.tamna.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PostBookingDataDto {
    private int classes;
    private int roomId;
    private String roomType;
    private String userId;
    private String userName;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
//    private LocalTime startTime;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
//    private LocalTime endTime;
    private String startTime;
    private String endTime;
    private List<String> teamMate;
//    private boolean official;


    public PostBookingDataDto(int classes, int roomId, String userId, String userName, String startTime, String endTime, List<String> teamMate
//                              ,boolean official
    ) {
        this.classes = classes;
        this.roomId = roomId;
        this.userId = userId;
        this.userName = userName;
//        String[] startTimeParsing = startTime.split(":");
//        String[] endTimeParsing = endTime.split(":");
//        this.startTime = LocalTime.of(Integer.parseInt(startTimeParsing[0]), Integer.parseInt(startTimeParsing[1]));
//        this.endTime = LocalTime.of(Integer.parseInt(endTimeParsing[0]), Integer.parseInt(endTimeParsing[1]));
        this.startTime = startTime;
        this.endTime = endTime;
        this.teamMate = teamMate;
//        this.official = official;
    }
}
