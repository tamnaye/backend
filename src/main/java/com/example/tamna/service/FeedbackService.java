package com.example.tamna.service;

import com.example.tamna.mapper.FeedbackMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class FeedbackService {

    private FeedbackMapper feedbackMapper;
    private long miliseconds = System.currentTimeMillis();
    private final Date today = new Date(miliseconds);
//    private Date today = Date.valueOf(LocalDate.now(ZoneId.of("Asia/Seoul")));

    @Autowired
    public FeedbackService(FeedbackMapper feedbackMapper) {
        this.feedbackMapper = feedbackMapper;
    }

    public int insertFeedback(String userId, String content){
        return feedbackMapper.insertFeedback(userId, today, content);
    }

    public List<String> findFeedback(){
        return feedbackMapper.findAll();
    }
}
