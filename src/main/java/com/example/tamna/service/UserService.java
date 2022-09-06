package com.example.tamna.service;

import com.example.tamna.model.UserDto;
import com.example.tamna.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {

    private UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    // 한번에 select 하기 위한 유저들 데이터 문자열 변환
    public String changeString(String userData, Collection<String> teamMateData){
        StringBuilder sb = new StringBuilder();
        if(userData != null) {
            System.out.println("스트링 변환시 유저 아이디 있음!!");
            sb.append("'" + userData + "'");
            teamMateData.forEach(m -> sb.append(",'" + m + "'"));
            return sb.substring(0, sb.length());
        }else{
            teamMateData.forEach(m -> sb.append("'" + m + "',"));
            System.out.println(sb);
            return sb.substring(0, sb.length() -1);

        }


    }

    // 최대 기수 가져오기
    public int getMaxClasses(){
       return userMapper.findMaxClasses();
    }

    // 유저 아이디로 정보조회
    public UserDto getUserData(String userId){
        return userMapper.findByUserId(userId);
    }

    // 같은 기수 유저들 이름 모두 가져오기
    public List<String> getUserNames(int classes) {
        return userMapper.findUserNamesByClasses(classes);
    }

    // 같은 기수의 유저들 이름으로 정보 조회
    public List<UserDto> getUsersData(int classes, String userData, List<String> teamMateData){
        String users = changeString(userData, teamMateData);
        return userMapper.findByUserName(classes, users);
    }


}
