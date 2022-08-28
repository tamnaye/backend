package com.example.tamna.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Participants {
    private int bookingId;
    private String userId;
    private String userType; // 예약자 일때 true;
}