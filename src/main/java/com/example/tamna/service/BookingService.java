package com.example.tamna.service;

import com.example.tamna.dto.BookingDto;
import com.example.tamna.dto.RoomDto;
import com.example.tamna.mapper.BookingMapper;
import com.example.tamna.mapper.ParticipantsMapper;
import com.example.tamna.mapper.RoomMapper;
import com.example.tamna.mapper.UserMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@NoArgsConstructor
public class BookingService {

    private UserMapper userMapper;
    private RoomMapper roomMapper;
    private BookingMapper bookingMapper;
    private ParticipantsMapper participantsMapper;
    private Date today;

    @Autowired
    public BookingService(UserMapper userMapper, RoomMapper roomMapper, BookingMapper bookingMapper, ParticipantsMapper participantsMapper) {
        this.userMapper = userMapper;
        this.roomMapper = roomMapper;
        this.bookingMapper = bookingMapper;
        this.participantsMapper = participantsMapper;
        this.today = Date.valueOf(LocalDate.now(ZoneId.of("Asia/Seoul")));
    }


    // 회의실 목록 데이터
    public List<RoomDto> roomList(){
       return  roomMapper.AllFindRoom();
    }

    // 전체 회의실 예약 현황 가져오기
    public List<BookingDto> allRoomBookingState(){
        return bookingMapper.findAllRoomState(today);
    }

    // 회의실별 예약현황  << null 예외처리 해줘야함
    public List<BookingDto> roomBookingState(int roomId){
        return bookingMapper.findByRoomId(today, roomId);
    }

     // 층수 별 예약 현황
    public List<BookingDto> floorBookingData(int floor){
        return bookingMapper.findByFloor(today, floor);
    }


    // 회원 회의실 신청 확인
//    public List<ParticipantsDto> checkUser(String userId, List<String> teamMate){
//        return
//    }

    // 예약자 insert test
    public int insertMember(int bookingId, Map<String, Object> member){
        return participantsMapper.insertParticipants(bookingId, today, member);
    }


    // 회의실 예약
    public int updateBooking(int roomId, String startTime, String endTime) {
        return bookingMapper.insertBooking(today, roomId, startTime, endTime);
    }
    // 회의실 예약
//    public BookingDto updateBooking(int roomId, String startTime, String endTime){
//        BookingDto nowBookingState = bookingMapper.findRoomState(today, roomId, startTime, endTime);
//        System.out.println("nowBooking" + nowBookingState);
//
//        if(nowBookingState == null){
//
//        }
//    }
//        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
//        System.out.println(today);
//
//        return bookingMapper.updateBooking(today, roomId, startTime, endTime);
//    }


    // 현재시간, 사용자들 예약된 거에서 현재시간이랑 비교
//    public boolean check_use(){
//        userMapper.findByUserId(userId);
//
//    }

}
