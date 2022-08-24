package com.example.tamna.mapper;

import com.example.tamna.dto.ParticipantsDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.sql.Date;
import java.util.List;

@Mapper
public interface ParticipantsMapper {

    // 예약 신청자와 멤버들 오늘 예약 데이터 검색
    @Select("SELECT * FROM PARTICIPANTS WHERE DATES=#{today} AND USER_ID IN (${usersIdData})")
    List<ParticipantsDto> findByUsersId(@Param("today") Date today, @Param("usersIdData") String usersIdData);

    // 예약 신청자와 멤버들 insert
    @Insert("INSERT INTO PARTICIPANTS VALUES(#{today}, #{bookingId}, #{userId}, #{userType})")
    int insertParticipants (@Param("today") Date today, @Param("bookingId") int bookingId, @Param("userId") String userId, @Param("userType") boolean userType);


}
