package com.example.tamna.mapper;

import com.example.tamna.dto.BookingDataDto;
import com.example.tamna.model.Booking;
import com.example.tamna.model.Participants;
import org.apache.ibatis.annotations.*;

import java.sql.Date;
import java.util.List;

@Mapper
public interface ParticipantsMapper {

    // 예약 신청자와 멤버들 오늘 예약 데이터 검색
    @Select("SELECT * FROM PARTICIPANTS WHERE DATES=#{today} AND USER_ID IN (${usersIdData})")
    List<Participants> findByUsersId(@Param("today") Date today, @Param("usersIdData") String usersIdData);

    // 유저별 회의실 횟수
    @Select("SELECT * FROM BOOKING INNER JOIN ROOM USING(ROOM_ID) INNER JOIN PARTICIPANTS USING(BOOKING_ID) where DATES=#{today} AND ROOM_TYPE=#{roomType} AND MODE is null AND USER_ID=#{userId} AND USER_TYPE=#{userType}")
    List<Booking> selectBookingUser(@Param("today") Date today, @Param("roomType") String roomType, @Param("userId") String userId, @Param("userType") boolean userType);

    // 회의실예약 유저들 동시간대 회의실 사용 체크
    @Select("SELECT * FROM BOOKING INNER JOIN ROOM USING(ROOM_ID) INNER JOIN PARTICIPANTS USING(BOOKING_ID) INNER JOIN USER USING(USER_ID) where DATES=#{today} AND MODE is null AND CLASSES=#{classes} AND USER_NAME IN (${usersName}) AND (#{startTime} <= START_TIME AND START_TIME < #{endTime} OR #{startTime} < END_TIME AND END_TIME <= #{endTime} OR START_TIME <= #{startTime} AND #{endTime} < END_TIME)")
    List<BookingDataDto> selectUsingUsers(@Param("today") Date today, @Param("classes") int classes, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("usersName") String usersName);

    // 나박스 예약시 유저 동시간대 회의실 사용 체크
    @Select("SELECT * FROM BOOKING INNER JOIN ROOM USING(ROOM_ID) INNER JOIN PARTICIPANTS USING(BOOKING_ID) INNER JOIN USER USING(USER_ID) where DATES=#{today} AND USER_ID=#{userId} AND (#{startTime} <= START_TIME AND START_TIME < #{endTime} OR #{startTime} < END_TIME AND END_TIME <= #{endTime} OR START_TIME <= #{startTime} AND #{endTime} < END_TIME) ")
    List<BookingDataDto> selectUsingOnlyUser(@Param("today")Date today, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("userId") String userId);

    // 예약 신청자와 멤버들 insert
    @Insert("INSERT INTO PARTICIPANTS VALUES (#{bookingId}, #{userId}, #{userType})")
    int insertParticipants( @Param("bookingId") int bookingId, @Param("userId") String userId, @Param("userType") boolean userType);

    // 예약 취소시 예약자 삭제
    @Delete("DELETE FROM PARTICIPANTS WHERE BOOKING_ID=#{bookingId}")
    int deleteParticipants(int bookingId);

    // 여러 예약 예약자들 삭제
    @Delete("DELETE FROM PARTICIPANTS WHERE BOOKING_ID IN (${bookingsIdString})")
    int deleteSeveralParticipants(String bookingsIdString);

}
