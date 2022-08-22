package com.example.tamna.controller;

import com.example.tamna.dto.PostBookingDataDto;


import com.example.tamna.dto.UserDto;
import com.example.tamna.mapper.RoomMapper;
import com.example.tamna.mapper.UserMapper;
import com.example.tamna.service.BookingService;

import com.example.tamna.service.RoomService;
import com.example.tamna.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;
    private RoomService roomService;
    private UserService userService;

    private UserMapper userMapper;
    private RoomMapper roomMapper;

    @Autowired
    public BookingController(BookingService bookingService,RoomService roomService, UserService userService, UserMapper userMapper, RoomMapper roomMapper){
        this.bookingService = bookingService;
        this.roomService = roomService;
        this.userService = userService;
        this.userMapper = userMapper;
        this.roomMapper = roomMapper;
    }


    @GetMapping(value = "")
    public ResponseEntity<Map<String, Object>> getBookingState(@RequestParam("userId") String userId, @RequestParam("roomId") int roomId) {
        System.out.println(userId);
        System.out.println(roomId);

        Map<String, Object> map = new HashMap<>();
        map.put("roomData",bookingService.roomList());
        map.put("bookingData", bookingService.roomBookingState(roomId));
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @PostMapping(value = "/test")
    public ResponseEntity<String> updateBooking(@RequestBody PostBookingDataDto postBookingDataDto){
        StringBuilder sb = new StringBuilder();
        postBookingDataDto.getTeamMate().forEach(m -> sb.append("'"+ m +"',"));
//        sb.insert(0,"(");
//        sb.insert(sb.length()-1,")");
        String users = sb.substring(0, sb.length()-1);
        System.out.println(users);
        List<UserDto> a = userService.getMemberData(postBookingDataDto.getClasses(), users);
        System.out.println(a);


//        List<UserDto> teamMates = new ArrayList<>();
//        int classes = postBookingDataDto.getClasses();
//        postBookingDataDto.getTeamMate().forEach(m -> teamMates.add(userMapper.findByUserName(classes, m)));
//        teamMates.add(userMapper.findByUserId(postBookingDataDto.getUserId()));
//        System.out.println(teamMates);


//        System.out.println(userMapper.findByUserName(users));
//        System.out.println(userMapper.findUserByName(users));
//        System.out.println(users.getClass().getName());

//        bookingService.insertMember(1, )

        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    @ApiOperation(value="[완료] 메인 회의실, 예약 데이터 보내기")
    @GetMapping(value = "/main")
    public ResponseEntity<Map<String, Object>> getUsers(@RequestParam("floor") int floor){
        Map<String, Object> map = new HashMap<>();

        map.put("RoomData",roomService.getFloorRoom(floor));
        map.put("BookingData", bookingService.floorBookingData(floor));

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

}
