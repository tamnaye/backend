package com.example.tamna.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelDto {
    private int bookingId;
    private String userId;
    private Boolean userType;
}
