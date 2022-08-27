package com.example.tamna.mapper;

import com.example.tamna.dto.BookingUserDataDto;
import com.example.tamna.dto.ParticipantsDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.core.parameters.P;

import java.sql.Date;
import java.util.List;

@Mapper
public interface ParticipantsMapper {

    // 예약 신청자와 멤버들 오늘 예약 데이터 검색
    @Select("SELECT * FROM PARTICIPANTS WHERE DATES=#{today} AND USER_ID IN (${usersIdData})")
    List<ParticipantsDto> findByUsersId(@Param("today") Date today, @Param("usersIdData") String usersIdData);

    // nabox 신청했었는지 확인
    @Select("SELECT * FROM BOOKING INNER JOIN ROOM USING(ROOM_ID) INNER JOIN PARTICIPANTS USING(booking_id) where DATES=#{today} AND ROOM_TYPE=#{roomType} AND USER_ID IN (${usersId})")
    List<BookingUserDataDto> selectUsers(@Param("today") Date today, @Param("usersId") String usersId, @Param("roomType") String roomType);

    // nabox 회의실 예약 신청자 insert
    @Insert("INSERT INTO PARTICIPANTS VALUES (#{bookingId}, #{userId}, #{userType})")
    int insertNaboxApplican(@Param("bookingId") int bookingId, @Param("userId") String userId, @Param("userType") boolean userType);

    // 예약 신청자와 멤버들 insert
    @Insert("INSERT INTO PARTICIPANTS VALUES (#{bookingId}, #{userId}, #{userType})")
    int insertParticipants( @Param("bookingId") int bookingId, @Param("userId") String userId, @Param("userType") boolean userType);



}
