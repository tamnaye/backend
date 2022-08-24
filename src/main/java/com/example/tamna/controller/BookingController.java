package com.example.tamna.controller;

import com.example.tamna.dto.ParticipantsDto;
import com.example.tamna.dto.PostBookingDataDto;

import com.example.tamna.dto.UserDto;
import com.example.tamna.mapper.RoomMapper;
import com.example.tamna.mapper.UserMapper;
import com.example.tamna.service.BookingService;

import com.example.tamna.service.ParticipantsService;
import com.example.tamna.service.RoomService;
import com.example.tamna.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Part;
import java.util.*;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;
    private RoomService roomService;
    private UserService userService;
    private ParticipantsService participantsService;

    private UserMapper userMapper;
    private RoomMapper roomMapper;

    @Autowired
    public BookingController(BookingService bookingService,RoomService roomService, UserService userService, ParticipantsService participantsService, UserMapper userMapper, RoomMapper roomMapper){
        this.bookingService = bookingService;
        this.roomService = roomService;
        this.userService = userService;
        this.participantsService = participantsService;
        this.userMapper = userMapper;
        this.roomMapper = roomMapper;
    }


    @ApiOperation(value=" 현정쓰꺼! 층수 회의실 데이터, 회의실별 예약 현황")
    @GetMapping(value = "")
    public ResponseEntity<Map<String, Object>> getRoomBookingState(@RequestParam("floor") int floor, @RequestParam("roomId") int roomId) {
        Map<String, Object> map = new HashMap<>();
        map.put("roomData",roomService.getFloorRoom(floor));
        map.put("bookingData", bookingService.roomBookingState(roomId));
        return ResponseEntity.status(HttpStatus.OK).body(map);
    } // role=manager일 때 2, 3층 보낼 때 데이터를 두개 한번에 보내줘야하는지 확인해 봐야함


    // 예약 넣기 테스트
    @PostMapping(value = "/bookingTest")
    public ResponseEntity<String> update(@RequestBody PostBookingDataDto postBookingDataDto){
        bookingService.insertBooking(postBookingDataDto.getRoomId(), postBookingDataDto.getStartTime(), postBookingDataDto.getEndTime());
        return ResponseEntity.status(HttpStatus.OK ).body("success");
    }

    @PostMapping(value = "/test")
    public ResponseEntity<String> updateBooking(@RequestBody PostBookingDataDto postBookingDataDto){
        List<String> teamMateNames = postBookingDataDto.getTeamMate();
        List<UserDto> users = userService.getUsersData(postBookingDataDto.getClasses(), userService.changeString(postBookingDataDto.getUserName(), teamMateNames));
        System.out.println(users);

        List<ParticipantsDto> bookingUser = participantsService.checkBookingUser(postBookingDataDto.getUserId(), users);
        System.out.println(bookingUser);
        if(bookingUser.isEmpty()){
            int a = bookingService.insertBooking(postBookingDataDto.getRoomId(), postBookingDataDto.getStartTime(), postBookingDataDto.getEndTime());
            System.out.println(a);
        }
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    @ApiOperation(value=" 수빈쓰꺼! [완료] 메인 회의실, 예약 데이터 보내기", notes = "@Param(floor)가 2,3층이면 각 층 데이터 | 2,3 아니면 모든 층 데이터 전송")
    @ResponseBody
    @GetMapping(value = "/main")
    public ResponseEntity<Map<String, Object>> getUsers(@RequestParam("floor") int floor){
        Map<String, Object> map = new HashMap<>();
        if(floor == 2 || floor == 3) {
            map.put("RoomData", roomService.getFloorRoom(floor));
            map.put("BookingData", bookingService.floorBookingData(floor));
        }
        else {
            map.put("RoomData", roomService.roomList());
            map.put("BookingData", bookingService.allRoomBookingState());
        }
        return ResponseEntity.status(HttpStatus.OK).body(map);
        }
}
