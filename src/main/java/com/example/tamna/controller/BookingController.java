package com.example.tamna.controller;

import com.example.tamna.dto.*;

import com.example.tamna.mapper.RoomMapper;
import com.example.tamna.mapper.UserMapper;
import com.example.tamna.model.User;
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


    @ApiOperation(value = "[완료] nabox 예약", notes = "roomType이 nabox인 경우! => 필요한 데이터 {int classes, int roomId, String roomType, String userId, String userName, String StartTime, String EndTime;")
    @PostMapping(value = "/nabox") // 현재 회의실 사용하는지 확인해야됨..;;;;
    public ResponseEntity<Map<String, Object>> naboxBooking(@RequestBody PostBookingDataDto naboxBookingDataDto) {
        // 같은 날 나박스를 사용했는지 확인
        boolean notUsedNabox = participantsService.checkBookingUser(naboxBookingDataDto.getUserId(), naboxBookingDataDto.getRoomType());
        Map<String, Object> map = new HashMap<>();
        Map<String, String> arr = new HashMap<>();

        if (notUsedNabox && naboxBookingDataDto.getTeamMate().isEmpty()) {
            // 현재 동시간대 중복 예약 확인
//            boolean checkUsing = participantsService.checkUsingBooking(naboxBookingDataDto);
            List<String> checkUsing = participantsService.checkUsingBooking(naboxBookingDataDto);
//            if(checkUsing){
            if(!checkUsing.isEmpty()){
                System.out.println("현재 회의실 사용 중인 인재분 있음");
                arr.put("fail", "현재 다른 회의실을 이용중이세요!ㅠㅠ 동시간대 예약은 안됩니다!ㅠㅠ");
                map.put("message", arr);
                return ResponseEntity.status(HttpStatus.OK).body(map);
            }
            // 회의실이 예약되어있는지 확인
            boolean checkBooking = bookingService.findSameBooking(naboxBookingDataDto.getRoomId(), naboxBookingDataDto.getStartTime());
            if (checkBooking) {
                arr.put("fail", "이미 예약된 시간대의 나박스 입니다. ㅠㅠ");
                map.put("message", arr);
                return ResponseEntity.status(HttpStatus.OK).body(map);
            } else {
                // 예약하고 예약된 bookingId 반환
                int bookingId = bookingService.insertBooking(naboxBookingDataDto.getRoomId(), naboxBookingDataDto.getStartTime(), naboxBookingDataDto.getEndTime(), false);
                participantsService.insertNaboxApplicant(bookingId, naboxBookingDataDto.getUserId());
                arr.put("success", "예약 성공!");
                map.put("message", arr);
                return ResponseEntity.status(HttpStatus.OK).body(map);
            }
        } else {
            arr.put("fail", "NaBox 예약 횟수를 초과하였습니다.");
            map.put("message", arr);
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }
    };


    @ApiOperation(value = "[완료] nabox2 예약", notes = "roomType이 nabox인 경우! => 필요한 데이터 {int roomId, String roomType, String userId, String StartTime, String EndTime, boolean official;")
    @PostMapping(value = "/nabox2") // 현재 회의실 사용하는지 확인해야됨..;;;;
    public ResponseEntity<Map<String, Object>> naboxBooking2(@RequestBody NaboxBookingDataDto naboxBookingDataDto) {
        // 같은 날 나박스를 사용했는지 확인
        boolean notUsedNabox = participantsService.checkBookingUser(naboxBookingDataDto.getUserId(), naboxBookingDataDto.getRoomType());
        Map<String, Object> map = new HashMap<>();
        Map<String, String> arr = new HashMap<>();

        if (notUsedNabox) {
            // 현재 동시간대 중복 예약 확인
            boolean checkUsing = participantsService.checkUsingBookingForNabox(naboxBookingDataDto);
            if(checkUsing){
                System.out.println("현재 회의실 사용 중인 인재분이 있음");
                arr.put("fail", "현재 다른 회의실을 이용중이세요!ㅠㅠ 동시간대 예약은 안됩니다!ㅠㅠ");
                map.put("message", arr);
                return ResponseEntity.status(HttpStatus.OK).body(map);
            }
            // 회의실이 예약되어있는지 확인
            boolean checkBooking = bookingService.findSameBooking(naboxBookingDataDto.getRoomId(), naboxBookingDataDto.getStartTime());
            if (checkBooking) {
                arr.put("fail", "이미 예약된 시간대의 나박스 입니다. ㅠㅠ");
                map.put("message", arr);
                return ResponseEntity.status(HttpStatus.OK).body(map);
            } else {
                // 예약하고 예약된 bookingId 반환
                int bookingId = bookingService.insertBooking(naboxBookingDataDto.getRoomId(), naboxBookingDataDto.getStartTime(), naboxBookingDataDto.getEndTime(), false);
                participantsService.insertNaboxApplicant(bookingId, naboxBookingDataDto.getUserId());
                arr.put("success", "예약 성공!");
                map.put("message", arr);
                return ResponseEntity.status(HttpStatus.OK).body(map);
            }
        } else {
            arr.put("fail", "NaBox 예약 횟수를 초과하였습니다.");
            map.put("message", arr);
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }
    };


    @PostMapping(value = "/conference")
    public ResponseEntity<Map<String, Object>> conferenceRoomBooking(@RequestBody PostBookingDataDto postBookingDataDto){
        List<String> teamMateNames = postBookingDataDto.getTeamMate();
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> arr = new HashMap<>();
        // 팀원 선택 되었는지 체크
        if(!teamMateNames.isEmpty()){
            // 신청자 하루에 예약 한번 체크
            boolean checkBooking = participantsService.checkBookingUser(postBookingDataDto.getUserId(), postBookingDataDto.getRoomType());
            if(checkBooking) {
                // 예약 신청자들 중 회의실 사용중인 유저 체크
                List<String> checkUsing = participantsService.checkUsingBooking(postBookingDataDto);
                if(!checkUsing.isEmpty()){
                    System.out.println("현재 회의실 사용 중인 인재분이 있음");
                    arr.put("fail", "현재 회의실 사용 중인 인재분"+checkUsing+"이 포함되어 있어요!ㅠㅠ");
                    map.put("message", arr);
                    return ResponseEntity.status(HttpStatus.OK).body(map);
                }else{
                    // 신청하는 회의실이 예약 되었는지 확인
                    boolean usingBooking = bookingService.findSameBooking(postBookingDataDto.getRoomId(), postBookingDataDto.getStartTime());
                    if(usingBooking){
                        System.out.println("이미 예약이 완료된 회의실 입니다. ㅠㅠ");
                        arr.put("fail", "이미 예약이 완료된 회의실 입니다. ㅠㅠ");
                        map.put("message", arr);
                    }else{
                        // 회의실 예약
                        int bookingId = bookingService.insertBooking(postBookingDataDto.getRoomId(), postBookingDataDto.getStartTime(), postBookingDataDto.getEndTime(), false);
                        // 참여자들 insert
                        List<User> users = userService.getUsersData(postBookingDataDto.getClasses(), userService.changeString(postBookingDataDto.getUserName(), teamMateNames));
                        participantsService.insertParticipants(bookingId, users, teamMateNames);
                        arr.put("success", "예약 성공!");
                        map.put("message", arr);
                    }
                    return ResponseEntity.status(HttpStatus.OK).body(map);
                }
            } else{
                System.out.println("유저 applicant = true");
                arr.put("fail", "회의실 예약 횟수를 초과했어요!ㅠㅠ");
                map.put("message", arr);
                return ResponseEntity.status(HttpStatus.OK).body(map);
            }
        }else{
            System.out.println("회의실은 2인이상일 경우만 예약하실 수 있습니다.");
            arr.put("fail", "회의실은 2인이상일 경우만 예약할 수 있어요!ㅠㅠ");
            map.put("message", arr);
            return ResponseEntity.status(HttpStatus.OK).body(map);

        }
    };

//    @PostMapping(value = "/manager")
//    public ResponseEntity<Map<String, Object>> forOfficialSchedule(OfficialBookingDataDto officialBookingDataDto){
//        if(officialBookingDataDto.getClasses() == 0){
//
//        }
//    }


//    // 예약 넣기 테스트
//    @PostMapping(value = "/bookingTest")
//    public ResponseEntity<String> update(@RequestBody PostBookingDataDto postBookingDataDto){
//        bookingService.userIncludedBooking(postBookingDataDto.getUserId());
//        return ResponseEntity.status(HttpStatus.OK).body("success");
//    }

    @ApiOperation(value = "[완료] 캘린더 상세 예약 정보")
    @GetMapping(value = "/details-booking")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> detailsBookingData(@RequestParam("roomId") int roomId, @RequestParam("startTime") String startTime){
        Map<String, Object> map = new HashMap<>();
        DetailBookingDataDto detailData = bookingService.findDetailBookingData(roomId, startTime);
        map.put("detailBookingData", detailData);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
}
