package com.example.tamna.service;

import com.example.tamna.dto.BookingDto;
import com.example.tamna.dto.RoomDto;
import com.example.tamna.dto.UserDto;
import com.example.tamna.mapper.BookingMapper;
import com.example.tamna.mapper.RoomMapper;
import com.example.tamna.mapper.UserMapper;
import org.apache.ibatis.jdbc.Null;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class BookingService {

    private UserMapper userMapper;
    private RoomMapper roomMapper;
    private BookingMapper bookingMapper;

    public BookingService(UserMapper userMapper, RoomMapper roomMapper, BookingMapper bookingMapper) {
        this.userMapper = userMapper;
        this.roomMapper = roomMapper;
        this.bookingMapper = bookingMapper;

    }


    public List<RoomDto> getRoomList() {
        try {
            return roomMapper.AllFindRoom();
        }catch (NullPointerException e){
            return Collections.emptyList();
        }
    }

    public List<BookingDto> getBookingState(String roomId, LocalTime startTime, LocalTime endTime) {
        try {
            LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
            System.out.println(today);
            return bookingMapper.findByRoomId(today, roomId);
        }catch (NullPointerException e){
            return Collections.emptyList();
        }

    }


    // 현재시간, 사용자들 예약된 거에서 현재시간이랑 비교
//    public boolean check_use(){
//        userMapper.findByUserId(userId);
//
//    }

//    public List<UserDto> getUseUser(Set<String> userId){
//        System.out.println(userId);
//        return userMapper.findByUserId(userId);
//    }
//

}
