package com.example.tamna.mapper;

import com.example.tamna.dto.ClassFloorDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AdminMapper {

    @Select("SELECT DISTINCT CLASSES, FLOOR FROM USER ORDER BY CLASSES ASC")
    List<ClassFloorDto> getClassOfFloor();


    @Update("UPDATE USER SET FLOOR=#{floor} WHERE CLASSES=#{classes}")
    int updateFloor(@Param("floor") int floor, @Param("classes") int classes);

}
