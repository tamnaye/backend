package com.example.tamna.service;

import com.example.tamna.dto.ParticipantsDto;
import com.example.tamna.dto.UserDto;
import com.example.tamna.mapper.ParticipantsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


@Service
public class ParticipantsService {

    private ParticipantsMapper participantsMapper;
    private UserService userService;
    private Date today;


    @Autowired
    public ParticipantsService(ParticipantsMapper participantsMapper, UserService userService){
        this.participantsMapper = participantsMapper;
        this.userService = userService;
        this.today = Date.valueOf(LocalDate.now(ZoneId.of("Asia/Seoul")));
    }

    // 예약자 확인
    public List<ParticipantsDto> checkBookingUser(String userId, List<UserDto> users){
        List<String> teamMateId = new ArrayList<>();
        users.stream().filter(m -> teamMateId.add(m.getUserId()));
        String usersIdData = userService.changeString(userId, teamMateId);
        return participantsMapper.findByUsersId(today, usersIdData);
    }


    // 회의실 예약 신청자와 멤버 구분하여 insert
    public void insertParticipants(int bookingId, List<UserDto> usersData, List<String> teamMate){
        for(int i=0; i< usersData.toArray().length; i++){
            UserDto user = usersData.get(i);
            if(!teamMate.contains(user.getUserName())){
                participantsMapper.insertParticipants(today, bookingId, user.getUserId(), true);
            } participantsMapper.insertParticipants(today, bookingId, user.getUserId(), false);
        }
    }


}
