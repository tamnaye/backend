package com.example.tamna.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StaticDataMapper {

    @Insert("INSERT INTO USER(CLASSES, USER_ID, USER_NAME) VALUES(#{classes}, #{userId}, #{name})")
    void insertUser (@Param("classes") int classes, @Param("userId") String userId, @Param("name") String name);

    @Insert("INSERT INTO CONFERENCE_ROOM(floor, confer_room_num, room_name) VALUES(#{floor}, #{conferRoomNum}, #{roomName})")
    void insertConfer (int floor, int conferRoomNum, String roomName);
}
