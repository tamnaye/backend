package com.example.tamna.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data //getter setter
@NoArgsConstructor // 파라미터가 없는 기본 생성자 생성
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 생성
public class BookingDto {
    private int bookingId;
    private Date dates;
    private int roomId;
    private String startTime;
    private String endTime;
    private boolean special;
}




