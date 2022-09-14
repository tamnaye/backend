package com.example.tamna.Enum;

import com.example.tamna.RoomType;
import com.example.tamna.model.Room;
import org.junit.jupiter.api.Test;


public class EnumTest {

    // 변수 변환하여 찾기
    @Test
    public void enumTest(){
        String type = "meeting";
        String b = type.toUpperCase();
        System.out.println(b);
        RoomType a = RoomType.valueOf(b);
        System.out.println(a);
        System.out.println(a.lowerCase);
    }

    @Test
    public void equals(){
        String type ="nabox";

        RoomType roomType = RoomType.valueOf(type.toUpperCase());
        System.out.println(roomType);

        System.out.println(type.equals(roomType.lowerCase));
        System.out.println(type == roomType.lowerCase);
        System.out.println(type == "nabox");
        System.out.println(roomType.lowerCase == "nabox");
    }
}
