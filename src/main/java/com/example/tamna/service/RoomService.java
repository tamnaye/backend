package com.example.tamna.service;

import com.example.tamna.dto.RoomDto;
import com.example.tamna.mapper.RoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private RoomMapper roomMapper;

    @Autowired
    public RoomService(RoomMapper roomMapper) {
        this.roomMapper = roomMapper;
    }

    // 층별 회의실 데이터
    public List<RoomDto> getFloorRoom(int floor){
        return roomMapper.findFloorRoom(floor);
    }
}
