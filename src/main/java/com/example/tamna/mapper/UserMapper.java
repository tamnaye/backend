package com.example.tamna.mapper;

import com.example.tamna.dto.UserDto;
import com.example.tamna.sql.UserSQL;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserMapper {
    //같은기수의 이름으로 찾기
    @Select("SELECT * FROM USER WHERE user_name IN (#{users})")
    List<UserDto> findByUserName(String users);

    @SelectProvider(type = UserSQL.class, method = "findUserByName")
    List<UserDto> findUserByName(String users);
}
