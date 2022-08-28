package com.example.tamna.service;

import com.example.tamna.model.User;
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

    // 한번에 select 하기 위한 유저들 데이터 문자열 변환
    public String changeString(String userData, List<String> teamMateData){
        StringBuilder sb = new StringBuilder();
        sb.append("'" + userData + "'");
        teamMateData.forEach(m -> sb.append(",'" + m + "'"));
        return sb.substring(0, sb.length());
    }

    // 최대 기수 가져오기
    public int getMaxClasses(){
       return userMapper.findMaxClasses();
    }

    // 유저 아이디로 정보조회
    public User getUserData(String userId){
        return userMapper.findByUserId(userId);
    }

    // 같은 기수 유저들 이름 모두 가져오기
    public List<String> getUserNames(int classes) {
        System.out.println(userMapper.findUserNamesByClasses(classes).getClass().getName());
        return userMapper.findUserNamesByClasses(classes);
    }

    // 같은 기수의 유저들 이름으로 정보 조회
    public List<User> getUsersData(int classes, String users){
        return userMapper.findByUserName(classes, users);
    }


}
