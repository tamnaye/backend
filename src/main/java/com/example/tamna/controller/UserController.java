package com.example.tamna.controller;

import com.example.tamna.dto.DetailBookingDataDto;
import com.example.tamna.service.BookingService;
import com.example.tamna.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final BookingService bookingService;


    @ApiOperation(value="[완료] 현재 최대기수와 유저 데이터 보내기")
    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> resUserData(@RequestParam("userId") String userId){
        Map<String, Object> map = new HashMap<>();

        map.put("maxClasses", userService.getMaxClasses());
        map.put("userData", userService.getUserData(userId));
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }


    @ApiOperation(value = "[완료] 마이페이지 자기 예약 목록 보기")
    @GetMapping(value = "/mypage")
    public ResponseEntity<Map<String, Object>> myBookingState(@RequestParam("userId") String userId){
        System.out.println(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("userData", userService.getUserData(userId));
        List<DetailBookingDataDto> myBookingDetailDataList = bookingService.userIncludedBooking(userId);
        System.out.println(myBookingDetailDataList);
        map.put("myBookingDetailDataList", myBookingDetailDataList);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }



}
