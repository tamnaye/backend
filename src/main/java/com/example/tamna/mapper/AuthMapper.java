package com.example.tamna.mapper;


import com.example.tamna.model.Token;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AuthMapper {

    @Insert("INSERT INTO TOKEN(USERID, REFRESH_TOKEN) VALUES(#{userId}, #{refreshToken})")
    int insertToken(String userId, String refreshToken);

    @Select("SELECT * FROM TOKEN WHERE REFRESH_TOKEN=#{refreshToken}")
    Token findToken(String refreshToken);

}
