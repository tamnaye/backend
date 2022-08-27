package com.example.tamna.service;

import com.example.tamna.dto.BookingUserDataDto;
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

    // 나박스 회의실 예약자 insert
    public int insertNaboxApplicant(int bookingId, String userId){
        boolean userType = true;
        return participantsMapper.insertNaboxApplican(bookingId, userId, userType);
    }


    // nabox 사용 하루 한번 제한으로, 사용했었는지 확인
    public boolean checkNabox(String usersId, String roomType){
        if(roomType.equals("nabox")){
            List<BookingUserDataDto> user = participantsMapper.selectUsers(today, usersId, roomType);
            System.out.println(participantsMapper.selectUsers(today, usersId, roomType));
            if(user.isEmpty()){
                System.out.println(user.isEmpty());
                System.out.println("나박스 예약 한 적 없음. 예약 가능 함");
                return true;
            }else{
                System.out.println("나박스 예약한 적 있음 예약 불가능");
                return false;
            }
        }else{
            System.out.println("roomType이 'nabox'가 아닙니다.");
            return false;
        }
    }

    // 회의실 예약 신청자와 멤버 구분하여 insert
    public void insertParticipants(int bookingId, List<UserDto> usersData, List<String> teamMate){
        for(int i=0; i< usersData.toArray().length; i++){
            UserDto user = usersData.get(i);
            if(!teamMate.contains(user.getUserName())){
                participantsMapper.insertParticipants(bookingId, user.getUserId(), true);
            } participantsMapper.insertParticipants(bookingId, user.getUserId(), false);
        }
    }

    

}
