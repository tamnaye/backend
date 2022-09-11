package com.example.tamna.service;

import com.example.tamna.mapper.FeedbackMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackMapper feedbackMapper;


    public Date time() {
        long miliseconds = System.currentTimeMillis();
        return new Date(miliseconds);
    }

    public int insertFeedback(String userId, String content){
        Date today = time();
        return feedbackMapper.insertFeedback(userId, today, content);
    }

    public List<String> findFeedback(){
        return feedbackMapper.findAll();
    }
}
