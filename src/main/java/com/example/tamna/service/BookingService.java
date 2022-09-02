package com.example.tamna.service;

import com.example.tamna.dto.DetailBookingDataDto;
import com.example.tamna.model.Booking;
import com.example.tamna.mapper.BookingMapper;
import com.example.tamna.mapper.ParticipantsMapper;
import com.example.tamna.mapper.RoomMapper;
import com.example.tamna.mapper.UserMapper;
import com.example.tamna.model.JoinBooking;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@NoArgsConstructor
public class BookingService {

    private final Logger LOGGER = LoggerFactory.getLogger(BookingService.class);
    private UserMapper userMapper;
    private RoomMapper roomMapper;
    private BookingMapper bookingMapper;
    private ParticipantsMapper participantsMapper;
    private Date today;

    @Autowired
    public BookingService(UserMapper userMapper, RoomMapper roomMapper, BookingMapper bookingMapper, ParticipantsMapper participantsMapper) {
        this.userMapper = userMapper;
        this.roomMapper = roomMapper;
        this.bookingMapper = bookingMapper;
        this.participantsMapper = participantsMapper;
        this.today = Date.valueOf(LocalDate.now(ZoneId.of("Asia/Seoul")));
    }


    // 전체 회의실 예약 현황 가져오기
    public List<Booking> allRoomBookingState(){
        return bookingMapper.findAllRoomState(today);
    }

    // 회의실별 예약현황
    public List<Booking> roomBookingState(int roomId){
        return bookingMapper.findByRoomId(today, roomId);
    }

     // 층수 별 예약 현황
    public List<Booking> floorBookingData(int floor){
        return bookingMapper.findByFloor(today, floor);
    }

    // 현재 회의실 예약 되어 있는지 확인
    public boolean findSameBooking(int roomId, String startTime, String endTime){
        List<Booking> sameBooking = bookingMapper.findSameBooking(today, roomId, startTime, endTime);
        System.out.println(sameBooking);
        if(!sameBooking.isEmpty()){
            System.out.println("현재 예약된 회의실!");
            return true;
        }
            System.out.println("현재 예약 되어 있지 않은 회의실!");
            return false;
    }


    // 예약된 회의실 디테일 정보
    public List<DetailBookingDataDto> floorDetailBookingData(int floor){
        List<DetailBookingDataDto> floorDetailBooking = new ArrayList<>();
        List<Booking> floorBooking = bookingMapper.findByFloor(today, floor);
        System.out.println("floorBooking: " + floorBooking);
        for(int k=0; k < floorBooking.toArray().length; k ++){
            int roomId = floorBooking.get(k).getRoomId();
            System.out.println("roomId: " + roomId);
            String startTime = floorBooking.get(k).getStartTime();
            System.out.println("startTime: " + startTime);
            List<JoinBooking> detailData = bookingMapper.findDetailBookingData(today, roomId, startTime);
            System.out.println("detailData: " + detailData);
            DetailBookingDataDto combineData = new DetailBookingDataDto();
            if(!detailData.isEmpty()) {
                Map<String, String> applicants = new HashMap<>();
                List<String> teamMate = new ArrayList<>();
                for (int i = 0; i < detailData.toArray().length; i++) {
                    if (detailData.get(i).isUserType()) {
                        applicants.put("userId", detailData.get(i).getUserId());
                        applicants.put("userName", detailData.get(i).getUserName());
                        combineData.setApplicant(applicants);
                    } else {
                        teamMate.add(detailData.get(i).getUserName());
                    }
                }
                combineData.setBookingId(detailData.get(0).getBookingId());
                combineData.setRoomId(detailData.get(0).getRoomId());
                combineData.setRoomName(detailData.get(0).getRoomName());
                combineData.setStartTime(detailData.get(0).getStartTime());
                combineData.setEndTime(detailData.get(0).getEndTime());
                combineData.setRoomType(detailData.get(0).getRoomType());
                combineData.setOfficial(detailData.get(0).isOfficial());
                combineData.setParticipants(teamMate);

                floorDetailBooking.add(combineData);
            }
        }
        return floorDetailBooking;

    }

//    // 회의실별 예약된 디테일 정보
//    public DetailBookingDataDto findDetailBookingData(int roomId, String startTime){
//        List<JoinBooking> detailData = bookingMapper.findDetailBookingData(today, roomId, startTime);
//        System.out.println(bookingMapper.findDetailBookingData(today, roomId, startTime));
//        DetailBookingDataDto combineData = new DetailBookingDataDto();
//        if(!detailData.isEmpty()) {
//            Map<String, String> applicants = new HashMap<>();
//            List<String> teamMate = new ArrayList<>();
//            for (int i = 0; i < detailData.toArray().length; i++) {
//                if (detailData.get(i).isUserType()) {
//                    applicants.put("userId", detailData.get(i).getUserId());
//                    applicants.put("userName", detailData.get(i).getUserName());
//                    combineData.setApplicant(applicants);
//                } else {
//                    teamMate.add(detailData.get(i).getUserName());
//                }
//            }
//            combineData.setBookingId(detailData.get(0).getBookingId());
//            combineData.setRoomId(detailData.get(0).getRoomId());
//            combineData.setRoomName(detailData.get(0).getRoomName());
//            combineData.setStartTime(detailData.get(0).getStartTime());
//            combineData.setEndTime(detailData.get(0).getEndTime());
//            combineData.setRoomType(detailData.get(0).getRoomType());
//            combineData.setOfficial(detailData.get(0).isOfficial());
//            combineData.setParticipants(teamMate);
//
//            return combineData;
//        }else{
//            combineData = null;
//            return combineData;
//        }
//    }


    // 회의실 예약
    public int insertBooking(int roomId, String startTime, String endTime, boolean official) {
        bookingMapper.insertBooking(today, roomId, startTime, endTime, official);
        int bookingId = bookingMapper.selectResultInsert(today, roomId, startTime, endTime, official);
        return bookingId;
    }

    // bookingId들 한번에 검색
    public String addBookingId(List<Integer> bookingIdList){
        if(!bookingIdList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            bookingIdList.forEach(m -> sb.append("'" + m + "',"));
            return sb.substring(0, sb.length() - 1);
        }else{
            return null;
        }
    }

    // 내가 포함된 에약 데이터 조회
    public  List<DetailBookingDataDto> userIncludedBooking(String userId) {

        List<DetailBookingDataDto> allMyBookingData = new ArrayList<>();

        // 내가 예약된 예약정보의 bookingId들 받아옴
        List<Integer> bookingIdList = bookingMapper.findMyBookingId(today, userId);
        System.out.println("내가 예약된 BookingId" + bookingIdList);
        LOGGER.info("{}가 예약한 bookingId 리스트 : {}", userId, bookingIdList);
        String bookingIdString = addBookingId(bookingIdList);

        // 예약한 데이터가 있을 때
        if(bookingIdString != null){
            // bookingId들 문자열로 변환 후 한번에 데이터들 모두 조회
            List<JoinBooking> myBookingList = bookingMapper.findMyBookingData(bookingIdString);
            System.out.println(bookingMapper.findMyBookingData(addBookingId(bookingIdList)));

            if(!myBookingList.isEmpty()){
                for(int i : bookingIdList){
                    DetailBookingDataDto combineData = new DetailBookingDataDto();
                    List<String> teamMate = new ArrayList<>();
                    for(int j=0; j < myBookingList.toArray().length; j++){
                       if(myBookingList.get(j).getBookingId() == i){
                           if(myBookingList.get(j).isUserType()){
                               combineData.setBookingId(myBookingList.get(j).getBookingId());
                               combineData.setRoomId(myBookingList.get(j).getRoomId());
                               combineData.setRoomName(myBookingList.get(j).getRoomName());
                               combineData.setRoomType(myBookingList.get(j).getRoomType());
                               combineData.setStartTime(myBookingList.get(j).getStartTime());
                               combineData.setEndTime(myBookingList.get(j).getEndTime());
                               combineData.setOfficial(myBookingList.get(j).isOfficial());
                               combineData.setMode(myBookingList.get(j).getMode());
                               Map<String, String> applicant = new HashMap<>();
                               applicant.put("userId", myBookingList.get(j).getUserId());
                               applicant.put("userName", myBookingList.get(j).getUserName());
                               combineData.setApplicant(applicant);
                           }else{
                               teamMate.add(myBookingList.get(j).getUserName());
                           }
                       } // bookingId가 다를 때 최상위 for문으로 이동
                       continue;
                    }
                    combineData.setParticipants(teamMate);
                    System.out.println(combineData);
                    allMyBookingData.add(combineData);
                }
            }else{
                allMyBookingData.add(null);
            }
        }
        LOGGER.info("allMyBookingData: {}", allMyBookingData.isEmpty());
        return allMyBookingData;
    };

    // 예약 취소
    public String deleteBooking(int bookingId){
        int checkBookingDelete = bookingMapper.deleteBooking(bookingId);
        int checkParticipantsDelete = participantsMapper.deleteParticipants(bookingId);
        System.out.println(checkBookingDelete);
        System.out.println(checkParticipantsDelete);
        if(checkBookingDelete == 1 && checkParticipantsDelete >= 1){
            return "success";
        }
        return "fail";
    }


    // 공식일정으로 인한 예약 수정
    public String updateBooking(int roomId, String userId, String startTime, String endTime, boolean official) {
//        System.out.println(startTime);
        List<Booking> sameBooking = bookingMapper.findSameBooking(today, roomId, startTime, endTime);
            System.out.println(sameBooking);
        if (!sameBooking.isEmpty()) {
            List<Integer> bookingIdList = new ArrayList<>();
            for (int i = 0; i < sameBooking.toArray().length; i++) {
                bookingIdList.add(sameBooking.get(i).getBookingId());
            }
            System.out.println("bookingIdList: " + bookingIdList);
            String bookingsIdString = addBookingId(bookingIdList);
            int updateResultCount = bookingMapper.cancelBooking(bookingsIdString);
            System.out.println("updateCount" + updateResultCount);
        }
        int resultBookingId = insertBooking(roomId, startTime, endTime, official);
        int resultParticipant = participantsMapper.insertParticipants(resultBookingId, userId, true);
        System.out.println(resultBookingId + " + " + resultParticipant);
        return "success";

    }


}
