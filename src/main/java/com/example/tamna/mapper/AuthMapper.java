package com.example.tamna.mapper;

import com.example.tamna.dto.TokenDto;
import com.example.tamna.dto.UserDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AuthMapper {

    @Select("SELECT * FROM USER WHERE USER_ID=#{userId}")
    UserDto findUserId(String userId);

    @Insert("INSERT INTO TOKEN(USERID, REFRESH_TOKEN) VALUES(#{userId}, #{refreshToken})")
    void updateToken(String userId, String refreshToken);

}
