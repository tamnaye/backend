package com.example.tamna.service;

import com.example.tamna.dto.BookingDataDto;
import com.example.tamna.dto.PostBookingDataDto;
import com.example.tamna.model.Participants;
import com.example.tamna.model.User;
import com.example.tamna.mapper.ParticipantsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class ParticipantsService {

    private final Logger LOGGER = LoggerFactory.getLogger(ParticipantsService.class);

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
    public List<Participants> checkBookingUser(String userId, List<User> users){
        List<String> teamMateId = new ArrayList<>();
        users.stream().filter(m -> teamMateId.add(m.getUserId()));
        String usersIdData = userService.changeString(userId, teamMateId);
        return participantsMapper.findByUsersId(today, usersIdData);
    }//

    // 나박스 회의실 예약자 insert
    public int insertNaboxApplicant(int bookingId, String userId){
        return participantsMapper.insertParticipants(bookingId, userId, true);
    }//


    // 회의실 예약 신청자와 멤버 구분하여 insert
    public void insertParticipants(int bookingId, List<User> usersData, List<String> teamMate){
        for(int i=0; i< usersData.toArray().length; i++){
            User user = usersData.get(i);
            if(!teamMate.contains(user.getUserName())){
                participantsMapper.insertParticipants(bookingId, user.getUserId(), true);
            } else{
                participantsMapper.insertParticipants(bookingId, user.getUserId(), false);
            }
            System.out.println("인재분들 참가자 디비 insert 성공");
        }
    }//


    // 회의실 횟수 한번 제한을 위한 체크
    public boolean checkBookingUser(String roomType, String userId){
            List<BookingDataDto> user = participantsMapper.selectBookingUser(today, roomType, userId, true);
            System.out.println(participantsMapper.selectBookingUser(today, roomType, userId,true));
            if(user.isEmpty()){
                System.out.println(user.isEmpty());
                System.out.println(roomType +" 예약 한 적 없음. 예약 가능");
                return true;
            }else{
                System.out.println(roomType + " 예약한 적 있음 예약 불가능");
                return false;
            }
        }//


    // 회의실 예약시, 동시간대 예약 체크
    public Set<String> checkUsingBooking(PostBookingDataDto postBookingDataDto){
        List<BookingDataDto> usingCheck;

        if(postBookingDataDto.getRoomType().equals("meeting")) {
            String usersName = userService.changeString(postBookingDataDto.getUserName(), postBookingDataDto.getTeamMate());
            LOGGER.info("예약자 + 팀원들 이름: {}", usersName);
            usingCheck = participantsMapper.selectUsingUsers(today, postBookingDataDto.getClasses(), postBookingDataDto.getStartTime(), postBookingDataDto.getEndTime(), usersName);
        }else{
            usingCheck = participantsMapper.selectUsingOnlyUser(today, postBookingDataDto.getStartTime(), postBookingDataDto.getEndTime(), postBookingDataDto.getUserId());
            System.out.println(participantsMapper.selectUsingOnlyUser(today, postBookingDataDto.getStartTime(), postBookingDataDto.getEndTime(), postBookingDataDto.getUserId()));
        }
        LOGGER.info("현재 회의실 사용중인 유저들: {}", usingCheck);

        Set<String> usingUsers = new HashSet<>();
        if(usingCheck.isEmpty()){
            System.out.println("사용중인 사람 x -> 회의실 사용 가능");

        }else{
            usingCheck.forEach(m -> usingUsers.add(m.getUserName()));
            System.out.println(usingUsers + "현재 회의실 사용 중");
        }
        return usingUsers;
        }


};



