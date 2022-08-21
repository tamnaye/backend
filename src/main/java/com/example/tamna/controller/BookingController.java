package com.example.tamna.controller;

import com.example.tamna.dto.BookingDto;
import com.example.tamna.dto.PostBookingDataDto;
import com.example.tamna.dto.RoomDto;
import com.example.tamna.mapper.UserMapper;
import com.example.tamna.service.BookingService;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.bytebuddy.asm.Advice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private BookingService bookingService;
    private Set<String> teamMate;
    private UserMapper userMapper;

    public BookingController(UserMapper userMapper){
        this.userMapper = userMapper;
    }

//    @ApiOperation(value="GET 예약페이지", notes = "예약페이지 데이터 가져오기")
//    @GetMapping(value = "")
////    @ResponseBody
//    public ResponseEntity<Map<String, Object>> getBookingState(@RequestParam String userId, @RequestParam String roomId) throws NullPointerException {
//        // @ApiParam 매개변수에 대한 설명 및 설정을 위한 어노테이션
//
//
//        List<RoomDto> rooms = bookingService.getRoomList();
//
//        List<BookingDto> bookingState = bookingService.getBookingState(roomId);
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("roomData",rooms);
//        map.put("bookingData", bookingState);
//
//        return ResponseEntity.status(HttpStatus.OK).body(map);
//
//    }

    @PostMapping(value = "/test")
    public ResponseEntity<String> updateBooking(@RequestBody PostBookingDataDto bookingPostDataDto){
//        List<String> usersName = bookingPostDataDto.getTeamMate();
//        System.out.println(usersName);
        StringBuilder sb = new StringBuilder();
        bookingPostDataDto.getTeamMate().forEach(m -> sb.append("'"+ m +"',"));
        sb.insert(0,"(");
        sb.insert(sb.length()-1,")");
        String users = sb.substring(0, sb.length()-1);
        System.out.println(users);
//        System.out.println(userMapper.findByUserName(users));
        System.out.println(userMapper.findUserByName(users));
        System.out.println(users.getClass().getName());
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }


    @PostMapping(value = "/timetest")
    public ResponseEntity<String> checkTime(@RequestBody PostBookingDataDto bookingDataDto){
        System.out.println(bookingDataDto);

        return ResponseEntity.status(HttpStatus.OK).body("success");
    }
}
