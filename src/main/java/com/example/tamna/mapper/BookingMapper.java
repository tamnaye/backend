package com.example.tamna.mapper;

import com.example.tamna.dto.BookingDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.sql.Date;
import java.util.List;

@Mapper
public interface BookingMapper {

    // 오늘 모든 예약 현황
    @Select("SELECT * FROM BOOKING WHERE dates=#{today}")
    List<BookingDto> findAllRoomState(Date today);

    // 층수별 예약 현황
    @Select("SELECT * FROM BOOKING WHERE DATES=#{today} AND ROOM_ID LIKE '${floor}__' ORDER BY ROOM_ID ASC")
    List<BookingDto> findByFloor(@Param("today") Date today, @Param("floor") int floor);

    // 회의실별 예약 현황
    @Select("Select * FROM BOOKING WHERE DATES=#{today} AND ROOM_ID=#{roomId} ORDER BY START_TIME ASC")
    List<BookingDto> findByRoomId(@Param("today") Date today, @Param("roomId") int roomId);

    // 예약 데이터 삽입

    @Insert("INSERT INTO BOOKING(DATES, ROOM_ID, START_TIME, END_TIME) VALUES (#{today}, #{roomId}, #{startTime}, #{endTime})")
    int insertBooking(@Param("today") Date today, @Param("roomId") int roomId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    // 회의실 예약 확인
    @Select("Select * FROM BOOKING WHERE dates=#{today}, room_id=#{roomId}, start_time=#{startTime}, end_time=#{endTime}")
    BookingDto findRoomState(@Param("today") Date today, @Param("roomId") int roomId, @Param("startTime") String startTime, @Param("endTime") String endTime);
}
