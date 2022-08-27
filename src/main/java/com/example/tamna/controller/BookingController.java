package com.example.tamna.controller;

import com.example.tamna.dto.*;

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


    @ApiOperation(value="[완료]층수 회의실 데이터, 회의실별 예약 현황", notes = "@Param(floor)가 2,3층이면 각 층 데이터 | 2,3 아니면 모든 층 데이터 전송")
    @GetMapping(value = "")
    public ResponseEntity<Map<String, Object>> getRoomBookingState(@RequestParam("floor") int floor, @RequestParam("roomId") int roomId) {
        Map<String, Object> map = new HashMap<>();
        if (floor == 2 || floor == 3) {
            map.put("roomData", roomService.getFloorRoom(floor));
        }else{
            map.put("roomData", roomService.roomList());
        }
        map.put("bookingData", bookingService.roomBookingState(roomId));
        return ResponseEntity.status(HttpStatus.OK).body(map);
    };


    @ApiOperation(value=" [완료] 메인 회의실, 예약 데이터 보내기", notes = "@Param(floor)가 2,3층이면 각 층 데이터 | 2,3 아니면 모든 층 데이터 전송")
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
    };


    // 예약 넣기 테스트
    @PostMapping(value = "/bookingTest")
    public ResponseEntity<String> update(@RequestBody PostBookingDataDto postBookingDataDto){
//        bookingService.insertBooking(postBookingDataDto.getRoomId(), postBookingDataDto.getStartTime(), postBookingDataDto.getEndTime());
        return ResponseEntity.status(HttpStatus.OK ).body("success");
    }

    @ApiOperation(value = "[완료] nabox 예약", notes = "roomType이 nabox인 경우")
    @PostMapping(value = "/nabox")
    public ResponseEntity<Map<String, Object>> naboxBooking(@RequestBody NaboxBookingDataDto naboxBookingDataDto) {
        // 같은 날 나박스를 사용했는지 확인
        boolean notUsedNabox = participantsService.checkNabox(naboxBookingDataDto.getUserId(), naboxBookingDataDto.getRoomType());
        Map<String, Object> map = new HashMap<>();
        if (notUsedNabox) {
            // 회의실이 예약되어있는지 확인
            boolean checkbooking = bookingService.findSameBooking(naboxBookingDataDto.getRoomId(), naboxBookingDataDto.getStartTime());
            if (checkbooking) {
                // 예약하고 예약된 bookingId 반환
                int bookingId = bookingService.insertBooking(naboxBookingDataDto.getRoomId(), naboxBookingDataDto.getStartTime(), naboxBookingDataDto.getEndTime(), naboxBookingDataDto.isOfficial());
                participantsService.insertNaboxApplicant(bookingId, naboxBookingDataDto.getUserId());
                map.put("message", "success");
                return ResponseEntity.status(HttpStatus.OK).body(map);
            } else {
                Map<String, String> arr = new HashMap<>();
                arr.put("error", "이미 예약되었습니다");
                map.put("message", arr);
                return ResponseEntity.status(HttpStatus.OK).body(map);
            }
        } else {
            Map<String, String> arr = new HashMap<>();
            arr.put("error", "나박스 예약 횟수를 초과하였습니다");
            map.put("message", arr);
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }
    };


//        List<String> teamMateNames = postBookingDataDto.getTeamMate();
//        List<UserDto> users = userService.getUsersData(postBookingDataDto.getClasses(), userService.changeString(postBookingDataDto.getUserName(), teamMateNames));
//        System.out.println(users);
//
//        List<ParticipantsDto> bookingUser = participantsService.checkBookingUser(postBookingDataDto.getUserId(), users);
//        System.out.println(bookingUser);
//        System.out.println(bookingUser.isEmpty());
//        if(bookingUser.isEmpty()){
//            int a = bookingService.insertBooking(postBookingDataDto.getRoomId(), postBookingDataDto.getStartTime(), postBookingDataDto.getEndTime());
//            System.out.println(a);
//            }
//         else{
//         }
//        return ResponseEntity.status(HttpStatus.OK).body("success");

//    };



}
