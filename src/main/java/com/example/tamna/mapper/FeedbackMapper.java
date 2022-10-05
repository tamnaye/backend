package com.example.tamna.mapper;

import org.apache.ibatis.annotations.*;

import java.sql.Date;
import java.util.List;

@Mapper
public interface FeedbackMapper {

    @Select("SELECT CONTENT FROM FEEDBACK ORDER BY CONTENT DESC")
    List<String> findAllFeedback();

    @Insert("INSERT INTO FEEDBACK(USER_ID, DATES, CONTENT) VALUES(#{userId}, #{today}, #{content})")
    int insertFeedback(@Param("userId") String userId, @Param("today") Date today, @Param("content") String content);

    @Select("SELECT CONTENT FROM FEEDBACK WHERE USER_ID=#{userId} ORDER BY CONTENT DESC")
    List<String> findFeedbackById(String userId);

    @Delete("DELETE FROM FEEDBACK WHERE USER_ID=#{userId} AND CONTENT=#{content}")
    int deleteFeedback(@Param("userId") String userId, @Param("content") String content);
}
