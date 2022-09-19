package com.example.tamna.controller;

import com.example.tamna.dto.*;

import com.example.tamna.model.UserDto;
import com.example.tamna.service.*;

import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;
    private final RoomService roomService;
    private final UserService userService;
    private final ParticipantsService participantsService;
    private final AuthService authService;

    @ApiOperation(value = "[완료] 현정 내비", notes = "@Param(floor)가 2,3층이면 각 층 데이터 | 2,3 아니면 모든 층 데이터 전송")
    @GetMapping(value = "/room-data")
    public ResponseEntity<Map<String, Object>> getRoomBookingState(@RequestParam("floor") int floor){
        Map<String, Object> map = new HashMap<>();
        if (floor == 2 || floor == 3 || floor == 4) {
            map.put("roomData", roomService.getFloorRoom(floor));
        } else {
            map.put("roomData", roomService.roomList());
        }
        return ResponseEntity.status(HttpStatus.OK).body(map);
    };


    @ApiOperation(value = "[완료] 층수 회의실 데이터, 회의실별 예약 현황", notes = "@Param(floor)가 2,3층이면 각 층 데이터 | 2,3 아니면 모든 층 데이터 전송")
    @GetMapping(value = "")
//    public ResponseEntity<Map<String, Object>> getRoomBookingState(@RequestParam("roomId") int roomId, @RequestParam("userId") String userId, @RequestParam("classes") int classes){
    public ResponseEntity<Map<String, Object>> getRoomBookingState(@RequestParam("roomId") int roomId, HttpServletResponse response) {
            UserDto user = authService.checkUser(response);
            Map<String, Object> map = new HashMap<>();
        if (user != null) {
            map.put("userData", userService.getUserData(user.getUserId()));
            map.put("roomData", roomService.getRoomId(roomId));
            map.put("bookingData", bookingService.roomBookingState(roomId));
            map.put("namesData", userService.getUserNames(user.getClasses()));
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }else{
            map.put("message", "tokenFail");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map);
        }
    };

    @ApiOperation(value = " [완료] 메인 회의실, 예약 데이터 보내기", notes = "@Param(floor)가 2,3층이면 각 층 데이터 | 2,3 아니면 모든 층 데이터 전송")
    @ResponseBody
    @GetMapping(value = "/main")
    public ResponseEntity<Map<String, Object>> getUsers(@RequestParam("floor") int floor) {
        Map<String, Object> map = new HashMap<>();
        if (floor == 2 || floor == 3 || floor == 4) {
            map.put("RoomData", roomService.getFloorRoom(floor));
            map.put("BookingData", bookingService.floorBookingData(floor));
        } else {
            map.put("RoomData", roomService.roomList());
            map.put("BookingData", bookingService.allRoomBookingState());
        }
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }//


    @ApiOperation(value = " [완료] 예약 현황 페이지 데이터", notes = "@Param(floor)가 2,3층이면 각 층 데이터")
    @GetMapping(value = "/details-booking")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getBookingState(@RequestParam("floor") int floor) {
        Map<String, Object> map = new HashMap<>();
        map.put("RoomData", roomService.getFloorRoom(floor));
        map.put("BookingData", bookingService.floorDetailBookingData(floor));

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }


    @ApiOperation(value = " [완료] 예약하기")
    @PostMapping(value = "/conference")
    public ResponseEntity<Map<String, Object>> conferenceRoomBooking(@RequestBody PostBookingDataDto postBookingDataDto, HttpServletResponse response){

        UserDto user = authService.checkUser(response);
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> arr = new HashMap<>();

        if(user!= null){
        List<String> teamMateNames = postBookingDataDto.getTeamMate();

        String roomType;

        if(postBookingDataDto.getRoomType().equals("meeting")){
            roomType = "회의실";
        }else if(postBookingDataDto.getRoomType().equals("nabox")){
            roomType = "나박스";
        }else{
            roomType = "스튜디오";
        }
        System.out.println("roomType: " + roomType);

        if(user.getClasses() != 0 ){
            // 신청자 회의실 하루에 한번만 예약 체크 or 나박스 최대 시간 예약 체크
            Map<Boolean, String> checkBooking = participantsService.checkBookingUser(postBookingDataDto.getRoomType(), user.getUserId());
            // 신청자가 예약이 안되어 있으면 or 나박스 최대 시간 안채웠으면
            if (checkBooking.keySet().contains(true)){
                // 룸타입이 회의실일 경우인데 팀메이트가 비어있는 경우
                if(roomType.equals("회의실") && teamMateNames.isEmpty()){
                    arr.put("fail", "회의실은 2인이상일 경우만 예약하실 수 있습니다️.");
                    map.put("message", arr);
                    return ResponseEntity.status(HttpStatus.OK).body(map);
                }else{
                    // 동시간대 회의실 사용자
                    Set<String> usingUsers = participantsService.checkUsingBooking(user, postBookingDataDto);
                    if(!usingUsers.isEmpty() && (roomType.equals("회의실") || roomType.equals("스튜디오"))){
                        arr.put("fail", "현재 동시간대 예약중인" + usingUsers + "님이 포함되어 있습니다.");
                        map.put("message", arr);
                        return ResponseEntity.status(HttpStatus.OK).body(map);
                    }else if(!usingUsers.isEmpty()){ //나박스일 때
                        arr.put("fail", "동시간대 다른 회의실 예약은 불가합니다.");
                        map.put("message", arr);
                        return ResponseEntity.status(HttpStatus.OK).body(map);
                    }else{
                        // 신청하는 회의실 예약 확인
                        boolean usingRoom = bookingService.findSameBooking(postBookingDataDto.getRoomId(),postBookingDataDto.getStartTime(), postBookingDataDto.getEndTime());
                        if(usingRoom) {
                            arr.put("fail", "❌ 이미 예약이 완료된 회의실 입니다.");
                            map.put("message", arr);
                            return ResponseEntity.status(HttpStatus.OK).body(map);
                        }else {
                            // 예약 O
                            // 회의실 일때 // 스튜디오일때는
                            if(roomType.equals("회의실") || roomType.equals("스튜디오")){
                                int bookingId = bookingService.insertBooking(postBookingDataDto.getRoomId(), postBookingDataDto.getStartTime(), postBookingDataDto.getEndTime(), false);
                                LOGGER.info("예약 성공한 bookingId: " + bookingId);
                                if(roomType.equals("회의실")){
                                    // 유저들 이름 종합
                                    List<UserDto> users = userService.getUsersData(user.getClasses(),user.getUserName(), teamMateNames);
                                    participantsService.insertParticipants(bookingId, users, teamMateNames);
                                    arr.put("success", "회의실 예약 성공! ♥ ");
                                }else{
                                    participantsService.insertApplicant(bookingId, user.getUserId());
                                    arr.put("success", "스튜디오 예약 성공! 비밀번호는 매니저님께 문의해주세요! ♥");
                                }
                                map.put("message", arr);
                                return ResponseEntity.status(HttpStatus.OK).body(map);
                            }else{ // 나박스일때
                                boolean timeResult = participantsService.checkUsingTime(postBookingDataDto.getStartTime(), postBookingDataDto.getEndTime());
                                if(checkBooking.values().contains("add") && !timeResult) {
                                    arr.put("fail", "나박스 하루 최대 이용시간은 2시간 입니다.");
                                    map.put("message", arr);
                                }
                                else {
                                    int bookingId = bookingService.insertBooking(postBookingDataDto.getRoomId(), postBookingDataDto.getStartTime(), postBookingDataDto.getEndTime(), false);
//                                    LOGGER.info("예약 성공한 bookingId: " + bookingId);
                                    participantsService.insertApplicant(bookingId, user.getUserId());
                                    //LOGGER.info("나박스 예약 유저 데이터: " + participantsService.insertNaboxApplicant(bookingId, postBookingDataDto.getUserId()));
                                    arr.put("success", roomType + " 예약 성공! ♥ ");
                                    map.put("message", arr);
                                }
                                return ResponseEntity.status(HttpStatus.OK).body(map);
                            }
                        }
                    }
                }
            }
            else{
                if(roomType.equals("나박스")){
                    arr.put("fail", roomType + "예약은 하루 최대 두시간만 가능합니다.");
                }
                else{
                    arr.put("fail",  roomType+"예약은 하루에 한번만 가능합니다.");
                }
                map.put("message", arr);
                return ResponseEntity.status(HttpStatus.OK).body(map);
            }
        }else { // 매니저님들 공식일정 등록
            // 4층이 아닐경우 공식일정
            if(postBookingDataDto.getRoomId() / 100 != 4) {
                // 기존 같은 회의실, 같은 시간대 공식일정 확인
                List<Boolean> checkOfficial = bookingService.checkOfficial(postBookingDataDto.getRoomId(), postBookingDataDto.getStartTime(), postBookingDataDto.getEndTime());
                // 있을 경우
                if(!checkOfficial.isEmpty()){
                    arr.put("fail", "이미 공식일정이 등록되어있습니다.");
                    map.put("message", arr);
                }else { // 없을 경우
                    // 기존 인재들 예약 cancel상태로 변경 및 공식 일정 예약
                    int resultBookingId = bookingService.updateBooking(postBookingDataDto.getRoomId(),user.getUserId(), postBookingDataDto.getStartTime(), postBookingDataDto.getEndTime(), true);
                    // 누가 예약하고 쓰는지 참석자 등록
                    List<UserDto> users = userService.getUsersData(user.getClasses(), user.getUserName(), teamMateNames);
                    System.out.println(users);
                    participantsService.insertParticipants(resultBookingId, users, postBookingDataDto.getTeamMate());

                    arr.put("success", roomType + " 공식 일정 등록 완료 ✅");
                    map.put("message", arr);
                }
            }
            else{ // 4층일 경우
                boolean usingRoom = bookingService.findSameBooking(postBookingDataDto.getRoomId(),postBookingDataDto.getStartTime(), postBookingDataDto.getEndTime());
                if(usingRoom) {
                    arr.put("fail", "이미 예약이 완료된 회의실 입니다.");
                    map.put("message", arr);
                }else {
                    int bookingId = bookingService.insertBooking(postBookingDataDto.getRoomId(), postBookingDataDto.getStartTime(), postBookingDataDto.getEndTime(), false);
                    LOGGER.info("예약 성공한 bookingId: " + bookingId);
                    // 유저들 이름 종합
                    List<UserDto> users = userService.getUsersData(user.getClasses(), user.getUserName(), teamMateNames);
                    participantsService.insertParticipants(bookingId, users, teamMateNames);
                    arr.put("success", roomType + "예약 성공! ♥ ");
                    map.put("message", arr);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }
        }else{
            map.put("message", "tokenFail");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map);
        }
    }//

    @Data
    static class BookingId{
        private int bookingId;
    }

    @ApiOperation(value = "[완료] 예약 취소")
    @PostMapping(value ="/cancellation")
    public ResponseEntity<Map<String, Object>> cancelBooking(@RequestBody BookingId bookingId, HttpServletResponse response){
        Map<String, Object> map = new HashMap<>();

        UserDto user = authService.checkUser(response);

        if(user!= null) {
            int intBookingId = bookingId.bookingId;
            CancelDto booking = bookingService.selectBookingId(intBookingId, user.getUserId());
            String checkCancel;

            if(booking != null) {
                if (!booking.isOfficial()) {
                    // 인재 예약 삭제
                    checkCancel = bookingService.deleteBooking(intBookingId);
                } else {
                    // 공식 일정 삭제
                    checkCancel = bookingService.deleteOfficialBooking(booking);
                }

                // 결과 반환
                if (checkCancel.equals("success")) {
                    map.put(checkCancel, "예약 취소가 완료되었습니다");
                } else {
                    map.put(checkCancel, "예약 취소에 실패하였습니다.");
                }

                return ResponseEntity.status(HttpStatus.OK).body(map);
            }else{
                map.put("message", "예약 취소 오류");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map);
            }
        }else{
            map.put("message", "tokenFail");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map);
        }
    }



}
