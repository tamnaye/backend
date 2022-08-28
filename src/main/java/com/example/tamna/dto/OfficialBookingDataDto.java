package com.example.tamna.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfficialBookingDataDto {
    private int classes;
    private int roomId;
    private String userId;
    private String startTime;
    private String endTime;

}
