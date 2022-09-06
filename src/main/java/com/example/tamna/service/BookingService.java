package com.example.tamna.service;

import com.example.tamna.dto.CancelDto;
import com.example.tamna.dto.DetailBookingDataDto;
import com.example.tamna.model.Booking;
import com.example.tamna.mapper.BookingMapper;
import com.example.tamna.mapper.ParticipantsMapper;
import com.example.tamna.mapper.RoomMapper;
import com.example.tamna.mapper.UserMapper;
import com.example.tamna.model.JoinBooking;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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
    private UserService userService;
    private long miliseconds = System.currentTimeMillis();
    private final Date today = new Date(miliseconds);
//    private Date today =  Date.valueOf(LocalDate(ZoneId.of("Asia/Seoul")));

    @Autowired
    public BookingService(UserMapper userMapper, RoomMapper roomMapper, BookingMapper bookingMapper, ParticipantsMapper participantsMapper, UserService userService) {
        this.userMapper = userMapper;
        this.roomMapper = roomMapper;
        this.bookingMapper = bookingMapper;
        this.participantsMapper = participantsMapper;
        this.userService = userService;
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
        List<Integer> sameBooking = bookingMapper.findSameBooking(today, roomId, startTime, endTime);
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
        System.out.println(today);
        for(int k=0; k < floorBooking.toArray().length; k ++){
            int roomId = floorBooking.get(k).getRoomId();
            String startTime = floorBooking.get(k).getStartTime();
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

    // 회의실 예약 및 동시 예약 불가 처리
    public int insertBooking(int roomId, String startTime, String endTime, boolean official) {
        bookingMapper.insertBooking(today, roomId, startTime, endTime, official);
        List<Integer> bookingId = bookingMapper.selectResultInsert(today, roomId, startTime, endTime);
        System.out.println(bookingId);
        int minBookingId = Collections.min(bookingId);
        if(bookingId.toArray().length != 1){
            System.out.println("최소 bookingId: " + minBookingId);
            for(int i : bookingId){
                if(minBookingId != i){
                    bookingMapper.deleteBooking(i);
                    participantsMapper.deleteParticipants(i);
                }
            }
        }
        return minBookingId;
    }

    // bookingId들 한번에 검색하기 위한
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
//            System.out.println(bookingMapper.findMyBookingData(addBookingId(bookingIdList)));

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

    // bookingId를 통한 공식일정 검색
    public Booking selectBookingId(int bookingId){
        return bookingMapper.selectOfficial(bookingId);
    }

    // 공식일정인지 결과 확인
    public  List<Boolean> checkOfficial(int roomId, String startTime, String endTime){
        return bookingMapper.findSameTimeOfficial(today, roomId, startTime, endTime);
    }

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



    // 공식 일정 취소시 인재분들 예약 살리기
    public String deleteOfficialBooking(Booking booking) {
        // 동시간대 cancel된 예약 가져오기
        List<CancelDto> canceledBooking = bookingMapper.selectCancelBooking(today, booking.getRoomId(), booking.getStartTime(), booking.getEndTime());

        if (!canceledBooking.isEmpty()) {
            List<Integer> bookingIdList = new ArrayList<>();
            List<String> userIdList = new ArrayList<>();

            for (int i=0; i < canceledBooking.toArray().length; i++){
                bookingIdList.add(canceledBooking.get(i).getBookingId());
                userIdList.add(canceledBooking.get(i).getUserId());
            }

            // 유저 아이디들로 한번에 검색하기 위한 스트링 변환
            String usersIdString = userService.changeString(null, userIdList);
            System.out.println(usersIdString);
            System.out.println(today);

            // 유저 아이디로 다른 예약 확인
            List<String> otherBookingUserId = bookingMapper.findNotCancelMyBooking(today, usersIdString);
            System.out.println("cancel된거 말고 다른 예약 있는 유저 : " + otherBookingUserId);
            if(!otherBookingUserId.isEmpty()){
                for (String sameUserId : otherBookingUserId){
                    System.out.println("sameUserId" + sameUserId);
                    for(int i=0; i < canceledBooking.toArray().length; i++){
                        if(canceledBooking.get(i).getUserId().equals(sameUserId)){
                            System.out.println(bookingMapper.deleteBooking(bookingIdList.get(i)));
                            bookingMapper.deleteBooking(bookingIdList.get(i));
                            participantsMapper.deleteParticipants(bookingIdList.get(i));
                            bookingIdList.remove(i);
                            System.out.println(bookingIdList);
                        }

                    }
                }
            }
            System.out.println("중요한 bookingIdList" + bookingIdList);
            String bookingsIdString = addBookingId(bookingIdList);
            System.out.println(bookingsIdString);
            if(bookingsIdString != null){
                int updateResultCount = bookingMapper.updateBookingMode(bookingsIdString, null);
                System.out.println("updateResultCount :" + updateResultCount);
            }
        }

        int deleteBooking = bookingMapper.deleteBooking(booking.getBookingId());
        int deleteParticipants = participantsMapper.deleteParticipants(booking.getBookingId());
        System.out.println(deleteBooking + deleteParticipants);
        if (deleteBooking == 1 && deleteParticipants >= 1) {
            return "success";
        }
        return "fail";
    }


    // 공식일정으로 인한 예약 수정
    public int updateBooking(int roomId, String userId, String startTime, String endTime, boolean official) {
        List<Integer> sameBooking = bookingMapper.findSameBooking(today, roomId, startTime, endTime);
            System.out.println(sameBooking);
        if (!sameBooking.isEmpty()){
            String bookingsIdString = addBookingId(sameBooking);
            System.out.println(bookingsIdString);
            int updateResultCount = bookingMapper.updateBookingMode(bookingsIdString, "cancel");
            System.out.println("updateCount" + updateResultCount);
        }
        int resultBookingId = insertBooking(roomId, startTime, endTime, official);
        System.out.println("resultBookingId : " + resultBookingId);
        return resultBookingId;
    }


}
