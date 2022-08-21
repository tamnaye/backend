package com.example.tamna.mapper;

import com.example.tamna.dto.RoomDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoomMapper {

    @Select("Select * from ROOM")
    List<RoomDto> AllFindRoom();
}
