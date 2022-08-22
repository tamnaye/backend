package com.example.tamna.mapper;

import com.example.tamna.dto.RoomDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoomMapper {

    // 모든 층의 회의실 데이터
    @Select("SELECT * FROM ROOM")
    List<RoomDto> AllFindRoom();

    // 층별 회의실 데이터
    @Select("SELECT * FROM ROOM WHERE FLOOR=#{floor}")
    List<RoomDto> findFloorRoom(int floor);
}
