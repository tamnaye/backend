package com.example.tamna.service;

import com.example.tamna.dto.ClassFloorDto;
import com.example.tamna.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminMapper adminMapper;

    public List<ClassFloorDto> getClassOfFloorData(){
        List<ClassFloorDto> result = adminMapper.getClassOfFloor();
        System.out.println(result);
        result.remove(result.get(0));
        System.out.println(result);
        return result;
    }

    public String updateClassOfFloorData(ClassFloorDto classFloorDto){
        if(classFloorDto.getClasses()!= 0) {
            int result = adminMapper.updateFloor(classFloorDto.getFloor(), classFloorDto.getClasses());
            return "success";
        }
        return "fail";
    }


}
