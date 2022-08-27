package com.example.tamna.service;

import com.example.tamna.dto.BookingDto;
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


    // 전체 회의실 예약 현황 가져오기
    public List<BookingDto> allRoomBookingState(){
        return bookingMapper.findAllRoomState(today);
    }

    // 회의실별 예약현황
    public List<BookingDto> roomBookingState(int roomId){
        return bookingMapper.findByRoomId(today, roomId);
    }

     // 층수 별 예약 현황
    public List<BookingDto> floorBookingData(int floor){
        return bookingMapper.findByFloor(today, floor);
    }

    // 예약 되어 있는지 확인
    public boolean findSameBooking(int roomId, String startTime){
        BookingDto sameBooking = bookingMapper.findSameBooking(today, roomId, startTime);
        if(sameBooking == null){
            System.out.println("현재 예약 되어 있지 않음");
            return true;
        }else{
            System.out.println("현재 예약 되어 있음 에러!");
            return false;
        }
    }

    // 회의실 예약
    public int insertBooking(int roomId, String startTime, String endTime, boolean official) {
        bookingMapper.insertBooking(today, roomId, startTime, endTime, official);
        int bookingId = bookingMapper.selectResultInsert(today, roomId, startTime, endTime, official);
        return bookingId;
    }



    // 회원 회의실 신청 확인
//    public List<ParticipantsDto> checkUser(String userId, List<String> teamMate){
//        return
//    }



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


}
