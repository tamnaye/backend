package com.example.tamna.controller;

import com.example.tamna.dto.ClassFloorDto;
import com.example.tamna.dto.RoomTimeDto;
import com.example.tamna.service.AdminService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @ApiOperation(value = "기수별 층수 보기")
    @GetMapping("/view/class&floor")
    public ResponseEntity<Map<String, Object>> getClassOfFloor(){
        Map<String, Object> map = new HashMap<>();
        List<ClassFloorDto> result = adminService.getClassOfFloorData();
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
            map.put("message", "층수 변경이 완료되었습니다!");
        }else{
            map.put("message", "fail");
        }
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @ApiOperation(value = "회의실별 최대시간", notes = "floor가 2,3이 아닌경우 fail 처리")
    @GetMapping("/view/room")
    public ResponseEntity<Map<String, Object>> getRoomData(@RequestParam("floor")int floor){
        Map<String, Object> map = new HashMap<>();
        if(floor == 2 || floor == 3){
            adminService.getRoomData(floor);
            map.put("RoomData", adminService.getRoomData(floor));
        }else{
            map.put("RoomData", "fail");
        }
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @ApiOperation(value = "회의실별 최대 시간 수정")
    @PostMapping("/change/maxtime")
    public ResponseEntity<Map<String, Object>> updateRoomTIme(@RequestBody RoomTimeDto roomTimeDto){
        Map<String, Object> map = new HashMap<>();
        if(roomTimeDto.getFloor() == 2 || roomTimeDto.getFloor() == 3) {
            String result = adminService.updateRoomTime(roomTimeDto);
            if (result.equals("success")) {
                map.put("message", "최대 시간 변경이 완료되었습니다.");
            } else {
                map.put("message", result);
            }
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }
        map.put("message", "2,3층만 변경 가능합니다.");
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

}
