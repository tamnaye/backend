package com.example.tamna.controller;

import com.example.tamna.dto.ClassFloorDto;
import com.example.tamna.dto.RoomTimeDto;
import com.example.tamna.mapper.AdminMapper;
import com.example.tamna.model.Room;
import com.example.tamna.model.UserDto;
import com.example.tamna.service.AdminService;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AdminMapper adminMapper;

    @ApiOperation(value="최신기수 업데이트")
    @PostMapping("/insert/user")
    public ResponseEntity<Map<String, Object>> insertUserData(@RequestPart(required = false) MultipartFile file, HttpServletRequest request) throws IOException {
        String resourceSrc = request.getServletContext().getRealPath("/data/");
        Map<String, Object> map = new HashMap<>();
        try {
            File dest = new File(resourceSrc + file.getOriginalFilename());
            file.transferTo(dest);
            String result = adminService.updateUser(dest);

            if (result.equals("success")) {
                map.put("message", "최신기수 업로드가 완료되었습니다.");
            } else {
                map.put("message", "파일 오류<빈 파일인지, 파일 양식이 옳은지 확인 혹은 파일명을 바꿔주세요!>");
            }
        }catch (NullPointerException e){
            map.put("message", "파일을 선택해주세요.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(map);


    }

    @ApiOperation(value = "기수별 층수 보기")
    @GetMapping("/view/class&floor")
    public ResponseEntity<Map<String, Object>> getClassOfFloor(){
        Map<String, Object> map = new HashMap<>();
        List<ClassFloorDto> result =  adminMapper.getClassOfFloor();;
        if(!result.isEmpty()) {
            map.put("ClassOfFloorData", result);
        }
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @ApiOperation(value = "기수별 층수 데이터 바꾸기", notes = "기수가 0으로 들어올 경우 fail 처리")
    @PostMapping("/change/floor")
    public ResponseEntity<Map<String, Object>> changeFloor(@RequestBody ClassFloorDto changeFloorDto){
        Map<String, Object> map = new HashMap<>();
        String result = adminService.updateClassOfFloorData(changeFloorDto);
        if(result.equals("success")){
            System.out.println(changeFloorDto.getClasses() + "기 가 " + changeFloorDto.getFloor() + "로 변경되었음.");
            map.put("message", "층수 변경이 완료되었습니다!");
        }else{
            map.put("message", "fail");
        }
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @ApiOperation(value = "회의실별 최대시간 조회", notes = "floor가 2,3,4가 아닌경우 fail 처리")
    @GetMapping("/view/room")
    public ResponseEntity<Map<String, Object>> getRoomData(@RequestParam("floor")int floor){
        Map<String, Object> map = new HashMap<>();
        List<Room> result = adminMapper.getFloorRoomData(floor);
            if(!result.isEmpty()){
                map.put("RoomData", result);
            }else{
                map.put("message", "회의실 데이터 불러오기 실패");
            }
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @ApiOperation(value = "회의실별 최대 시간 수정", notes = "floor가 2,3,4가  아닌경우 변경불가 메시지")
    @PostMapping("/change/maxtime")
    public ResponseEntity<Map<String, Object>> updateRoomTIme(@RequestBody RoomTimeDto roomTimeDto){
        Map<String, Object> map = new HashMap<>();
        int result = adminMapper.updateTime(roomTimeDto.getMaxTime(), roomTimeDto.getRoomId());
        if (result != 0) {
            map.put("message", "최대 시간 변경이 완료되었습니다.");
        } else {
            map.put("message", "최대 시간 변경 실패");
        }
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }


    @ApiOperation(value = "전체 기수리스트 보내기")
    @GetMapping("/view/class-list")
    public ResponseEntity<Map<String, Object>> getAllClassList(){
        Map<String, Object> map = new HashMap<>();
        List<Integer> classListResult = adminMapper.getAllClass();
        map.put("ClassList", classListResult);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @ApiOperation(value="기수별 유저데이터 보내기")
    @GetMapping("/view/user")
    public ResponseEntity<Map<String, Object>> getAllUserData(@RequestParam("classes") int classes) {
        Map<String, Object> map = new HashMap<>();
        List<UserDto> result = adminMapper.allSelectUser(classes);
        map.put("AllUserData", result);
       return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @ApiOperation(value="유저 데이터 수정")
    @PostMapping("/update/user")
    public ResponseEntity<Map<String, Object>> updateUserData(@RequestBody UserDto userDto){
        Map<String, Object> map = new HashMap<>();
        System.out.println(userDto);
        int result = adminMapper.updateUserData(userDto.getUserName(), userDto.getRoles(), userDto.getFloor(), userDto.getUserId());
        if(result == 1){
            map.put("message", userDto.getUserName() + "님의 데이터가 수정되었습니다.");
        }else{
            map.put("message", "데이터 업데이트 실패");
        }
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }


    @ApiOperation(value = "유저 데이터 삽입")
    @PostMapping("insertion/user")
    public ResponseEntity<Map<String, Object>> insertUserData(@RequestBody UserDto userDto){
        Map<String, Object> map = new HashMap<>();
        int result = adminMapper.insertUserData(userDto.getClasses(), userDto.getUserId(), userDto.getUserName(), userDto.getRoles(), userDto.getFloor());
        if(result == 1){
            map.put("message", userDto.getUserName() + "님의 데이터가 추가되었습니다.");
        }else{
            map.put("message", "데이터 추가 실패");
        }
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }


    @Data
    static class UserId{
        private String userId;
    }

    @ApiOperation(value="유저 데이터 삭제")
    @PostMapping("/deletion/user")
    public ResponseEntity<Map<String, Object>> deleteUserData(@RequestBody UserId userId){
        String getUserId = userId.userId;
        Map<String, Object> map = new HashMap<>();
        int result = adminMapper.deleteUser(getUserId);
        if(result == 1){
            map.put("message", "데이터 삭제가 완료되었습니다.");
        }else{
            map.put("message", "데이터 삭제 실패");
        }
        return ResponseEntity.status(HttpStatus.OK).body(map);

    }



}
