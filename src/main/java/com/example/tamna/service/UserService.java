package com.example.tamna.service;

import com.example.tamna.dto.UserDto;
import com.example.tamna.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    // 최대 기수 가져오기
    public int getMaxClasses(){
       return userMapper.findMaxClasses();
    }

    // 유저 아이디로 정보조회
    public UserDto getUserData(String userId){
        return userMapper.findByUserId(userId);
    }

    // 같은 기수의 유저 이름으로 정보 조회
    public List<UserDto> getMemberData(int classes, String users){
        return userMapper.findByUserName(classes, users);
    }
}
