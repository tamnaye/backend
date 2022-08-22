package com.example.tamna.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.Map;

@Mapper
public interface ParticipantsMapper {

//    @Select("SELECT JSON_EXTRACT() FROM PARTICIPANTS WHERE DATES=#{today}")

    @Insert("INSERT INTO PARTICIPANTS(BOOKING_ID, DATES, MEMBER) VALUES(#{bookingId}, #{today}, #{member})")
    int insertParticipants(@Param("bookingId") int bookingId, @Param("today") Date today, @Param("member") Map<String, Object> member);
}
