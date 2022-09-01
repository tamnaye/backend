package com.example.tamna.mapper;

import com.example.tamna.model.Booking;
import com.example.tamna.model.JoinBooking;
import org.apache.ibatis.annotations.*;


import java.sql.Date;
import java.util.List;

@Mapper
public interface BookingMapper {

    // 오늘 모든 예약 현황
    @Select("SELECT * FROM BOOKING WHERE dates=#{today} AND MODE is null ORDER BY ROOM_ID ASC")
    List<Booking> findAllRoomState(Date today);

    // 층수별 예약 현황
    @Select("SELECT * FROM BOOKING WHERE DATES=#{today} AND MODE is null AND ROOM_ID LIKE '${floor}__' ORDER BY ROOM_ID ASC")
    List<Booking> findByFloor(@Param("today") Date today, @Param("floor") int floor);

    // 회의실별 예약 현황
    @Select("SELECT * FROM BOOKING WHERE DATES=#{today} AND MODE is null AND ROOM_ID=#{roomId} ORDER BY START_TIME ASC")
    List<Booking> findByRoomId(@Param("today") Date today, @Param("roomId") int roomId);

    // 예약 데이터 삽입
    @Insert("INSERT INTO BOOKING(DATES, ROOM_ID, START_TIME, END_TIME, OFFICIAL) VALUES (#{today}, #{roomId}, #{startTime}, #{endTime}, #{official})")
//    @Options(useGeneratedKeys = true, keyProperty = "bookingId")
//    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty = "bookingId", before = false, resultType=int.class)
    void insertBooking(@Param("today") Date today, @Param("roomId") int roomId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("official") boolean official);

    // 예약 결과 확인
    @Select("SELECT BOOKING_ID FROM BOOKING WHERE DATES=#{today} AND ROOM_ID=#{roomId} AND START_TIME=#{startTime} AND END_TIME=#{endTime} AND OFFICIAL=#{official}")
    int selectResultInsert(@Param("today") Date today, @Param("roomId") int roomId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("official") boolean official);

    // 회의실 예약 확인
    @Select("SELECT * FROM BOOKING WHERE DATES=#{today} AND ROOM_ID=#{roomId} AND (#{startTime} <= START_TIME AND START_TIME < #{endTime} OR #{startTime} < END_TIME AND END_TIME <= #{endTime})")
    List<Booking> findSameBooking(@Param("today") Date today, @Param("roomId") int roomId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    // 내가 포함된 예약 bookingId 조회
    @Select("SELECT BOOKING_ID FROM BOOKING INNER JOIN PARTICIPANTS USING(BOOKING_ID) where DATES=#{today} AND USER_ID=#{userId} ORDER BY START_TIME ASC")
    List<Integer> findMyBookingId(@Param("today") Date today, @Param("userId") String userId);

    // 내가 포함된 bookingId를 통해 예약관련 모두 조회
    @Select("SELECT * FROM BOOKING INNER JOIN ROOM USING(ROOM_ID) INNER JOIN PARTICIPANTS USING(BOOKING_ID) INNER JOIN USER USING(USER_ID) where BOOKING_ID IN (${bookingIdList}) ORDER BY START_TIME ASC, USER_TYPE DESC")
    List<JoinBooking> findMyBookingData(@Param("bookingIdList") String bookingIdList);

    // 예약된 회의실 별 디테일 정보
    @Select("SELECT * FROM BOOKING INNER JOIN ROOM USING(ROOM_ID) INNER JOIN PARTICIPANTS USING(BOOKING_ID) INNER JOIN USER USING(USER_ID) where DATES=#{today} AND ROOM_ID=#{roomId} AND MODE is null AND START_TIME=#{startTime}")
    List<JoinBooking> findDetailBookingData(@Param("today") Date today, @Param("roomId") int roomId, @Param("startTime") String startTime);

    // 예약 취소
    @Delete("DELETE FROM BOOKING WHERE BOOKING_ID=#{bookingId}")
    int deleteBooking(int bookingId);

    @Update("UPDATE BOOKING SET MODE = 'cancel' WHERE BOOKING_ID IN (${bookingsId})")
    int cancelBooking(String bookingsId);

}

