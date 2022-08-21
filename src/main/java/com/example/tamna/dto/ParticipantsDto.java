package com.example.tamna.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantsDto {
    private int bookingId;
    private Map<String, Object> participants;

}