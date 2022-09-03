package com.example.tamna.controller;

import com.example.tamna.model.Feedback;
import com.example.tamna.service.FeedbackService;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class FeedbackController {

    private FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @ApiOperation(value = "[완료] 피드백 내용 보내기")
    @PostMapping(value = "/feedback")
    public ResponseEntity<Map<String, Object>> feedback(@RequestBody Feedback feedback){
        int result = feedbackService.insertFeedback(feedback.getUserId(), feedback.getContent());
        Map<String, Object> map = new HashMap<>();
        if(result == 1) {
            map.put("message", "success");
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }
        else{
            map.put("message", "fail");
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }
    }

    @ApiOperation(value= "[완료] 피드백 페이지")
    @GetMapping(value = "/feedback")
    public ResponseEntity<Map<String, Object>> getFeedback(){
        Map<String, Object> map = new HashMap<>();
        map.put("FeedbackData",feedbackService.findFeedback());
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
}
