package com.example.tamna.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinBooking {
    private int bookingId;
    private String startTime;
    private String endTime;
    private boolean official;
    private int roomId;
    private String roomName;
    private String roomType;
    private String userId;
    private String userName;
    private boolean userType;
}
