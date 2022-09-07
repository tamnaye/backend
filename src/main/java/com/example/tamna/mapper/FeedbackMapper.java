package com.example.tamna.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.sql.Date;
import java.util.List;

@Mapper
public interface FeedbackMapper {

    @Select("SELECT CONTENT FROM FEEDBACK ORDER BY DATES DESC")
    List<String> findAll();

    @Insert("INSERT INTO FEEDBACK(USER_ID, DATES, CONTENT) VALUES(#{userId}, #{today}, #{content})")
    int insertFeedback(@Param("userId") String userId, @Param("today") Date today, @Param("content") String content);
}
