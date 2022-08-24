package com.example.tamna.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantsDto {
    private Date dates;
    private int bookingId;
    private String userId;
    private String userType; // 예약자 일때 true;

}