package com.example.tamna.mapper;

import com.example.tamna.dto.BookingDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface BookingMapper {

    @Select("Select * FROM BOOKING WHERE dates=#{today}, room_id=#{roomId}")
    List<BookingDto> findByRoomId(LocalDate today, String roomId);
}
