package com.example.tamna.mapper;

import com.example.tamna.dto.ClassFloorDto;
import com.example.tamna.dto.RoomTimeDto;
import com.example.tamna.model.UserDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminMapper {

    // 최신기수 인재들 업데이트(중복시 pass 없을시엔 insert)
    @Insert("INSERT IGNORE INTO USER(CLASSES, USER_ID, USER_NAME, FLOOR) VALUES(#{classes}, #{userId}, #{userName}, 3)")
    int insertUser (@Param("classes") int classes, @Param("userId") String userId, @Param("userName") String name);

    // 기수별 층수 조회
    @Select("SELECT DISTINCT CLASSES, FLOOR FROM USER WHERE CLASSES NOT IN (0) ORDER BY CLASSES ASC")
    List<ClassFloorDto> getClassOfFloor();

    // 기수별 층수 수정
    @Update("UPDATE USER SET FLOOR=#{floor} WHERE CLASSES=#{classes}")
    int updateFloor(@Param("floor") int floor, @Param("classes") int classes);

    // 회의실 별 시간 제한 가져오기
    @Select("SELECT FLOOR, ROOM_NAME, MAX_TIME FROM ROOM WHERE FLOOR=#{floor}")
    List<RoomTimeDto> getFloorRoomData(int floor);

    @Update("UPDATE ROOM SET MAX_TIME=#{maxTime} WHERE ROOM_NAME=#{roomName}")
    int updateTime(@Param("maxTime") int maxTime, @Param("roomName") String roomName);

    // 모든 유저 데이터 보내기
    @Select("SELECT * FROM USER ORDER BY CLASSES DESC")
    List<UserDto> allSelectUser();

    // 기수 리스트
    @Select("SELECT DISTINCT CLASSES FROM USER")
    List<Integer> getAllClass();


}
