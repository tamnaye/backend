package com.example.tamna.service;

import com.example.tamna.dto.ClassFloorDto;
import com.example.tamna.dto.RoomTimeDto;
import com.example.tamna.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminMapper adminMapper;

    // 최신 인재들 업데이트
    public String updateUser(File dest) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(dest));
        String line;
        int result = 0;
        if((line= br.readLine())!=null) {
            while ((line = br.readLine()) != null) {
                String[] datalines = line.split(",");
                try {
                    int classes = Integer.parseInt(datalines[0]);
                    String userId = datalines[1];
                    String name = datalines[2];
                    System.out.println(classes + " " + userId + " " + name);
                    result = adminMapper.insertUser(classes, userId, name);
                } catch (NumberFormatException e) {
                    continue;
                }
            }
            br.close();
            if(result >= 1){
                return "success";
            }
        }
        return "fail";

    }

    // 기수별 층수 데이터
    public List<ClassFloorDto> getClassOfFloorData(){
        List<ClassFloorDto> result = adminMapper.getClassOfFloor();
        System.out.println(result);
        result.remove(result.get(0));
        System.out.println(result);
        return result;
    }

    // 기수별 층수 데이터 수정
    public String updateClassOfFloorData(ClassFloorDto classFloorDto){
        if(classFloorDto.getClasses()!= 0) {
            int result = adminMapper.updateFloor(classFloorDto.getFloor(), classFloorDto.getClasses());
            return "success";
        }
        return "fail";
    }

    // 회의실 별 최대시간 조회
    public List<RoomTimeDto> getRoomData(int floor){
        List<RoomTimeDto> result = adminMapper.getFloorRoomData(floor);
        return result;
    }


    // 회의실 최대 예약 시간 수정
    public String updateRoomTime(RoomTimeDto roomTimeDto){
        int result = adminMapper.updateTime(roomTimeDto.getMaxTime(), roomTimeDto.getRoomName());
        if(result != 0){
            return "success";
        }
        return "fail";
    }




}
