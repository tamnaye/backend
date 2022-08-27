package com.example.tamna.mapper;

import com.example.tamna.dto.BookingDto;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.parameters.P;

import java.sql.Date;
import java.util.List;

@Mapper
public interface BookingMapper {

    // 오늘 모든 예약 현황
    @Select("SELECT * FROM BOOKING WHERE dates=#{today} ORDER BY ROOM_ID ASC")
    List<BookingDto> findAllRoomState(Date today);

    // 층수별 예약 현황
    @Select("SELECT * FROM BOOKING WHERE DATES=#{today} AND ROOM_ID LIKE '${floor}__' ORDER BY ROOM_ID ASC")
    List<BookingDto> findByFloor(@Param("today") Date today, @Param("floor") int floor);

    // 회의실별 예약 현황
    @Select("Select * FROM BOOKING WHERE DATES=#{today} AND ROOM_ID=#{roomId} ORDER BY START_TIME ASC")
    List<BookingDto> findByRoomId(@Param("today") Date today, @Param("roomId") int roomId);

    // 예약 데이터 삽입
    @Insert("INSERT INTO BOOKING(DATES, ROOM_ID, START_TIME, END_TIME, OFFICIAL) VALUES (#{today}, #{roomId}, #{startTime}, #{endTime}, #{official})")
//    @Options(useGeneratedKeys = true, keyProperty = "bookingId")
//    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty = "bookingId", before = false, resultType=int.class)
    void insertBooking(@Param("today") Date today, @Param("roomId") int roomId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("official") boolean official);

    // 예약 결과 확인
    @Select("SELECT BOOKING_ID FROM BOOKING WHERE DATES=#{today} AND ROOM_ID=#{roomId} AND START_TIME=#{startTime} AND END_TIME=#{endTime} AND official=#{official}")
    int selectResultInsert(@Param("today") Date today, @Param("roomId") int roomId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("official") boolean official);

    // 회의실 예약 확인
    @Select("Select * FROM BOOKING WHERE DATES=#{today} AND ROOM_ID=#{roomId} AND START_TIME <= #{startTime} AND #{startTime} < END_TIME")
    BookingDto findSameBooking(@Param("today") Date today, @Param("roomId") int roomId, @Param("startTime") String startTime);
}
