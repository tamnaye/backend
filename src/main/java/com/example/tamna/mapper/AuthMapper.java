package com.example.tamna.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {

    @Insert("INSERT INTO TOKEN(USERID, REFRESH_TOKEN) VALUES(#{userId}, #{refreshToken})")
    void updateToken(String userId, String refreshToken);

}
