package com.example.tamna.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NaboxBookingDataDto {
    private int roomId;
    private String roomType;
    private String userId;
    private String StartTime;
    private String EndTime;
    private boolean official;
}
