package com.example.tamna.sql;


import com.example.tamna.dto.UserDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Set;

public class UserSQL {

    public static final String FIND_MEMBER =
            "SELECT * FROM USER";

    public String findUserByName(String users) {

        SQL query = new SQL() {
            {
                SELECT("*").FROM("USER").
                WHERE("user_name IN #{users}");
            }
        };
        System.out.println(query);
        return query.toString();
    }

}
