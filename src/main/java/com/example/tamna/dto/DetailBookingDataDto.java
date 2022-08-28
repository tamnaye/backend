package com.example.tamna.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailBookingDataDto {
    private int bookingId;
    private int roomId;
    private String roomName;
    private String roomType;
    private String startTime;
    private String endTime;
    private Map<String, String> applicant;
    private List<String> participants;
    private boolean official;
}
