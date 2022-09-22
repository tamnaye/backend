package com.example.tamna.controller;

import com.example.tamna.model.Feedback;
import com.example.tamna.service.FeedbackService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;


    @ApiOperation(value = "피드백 내용 보내기")
    @PostMapping(value = "/feedback")
    public ResponseEntity<Map<String, Object>> feedback(@RequestBody Feedback feedback){
            Map<String, Object> map = new HashMap<>();
        if(feedback.getUserId()!= null) {
            int result = feedbackService.insertFeedback(feedback.getUserId(), feedback.getContent());
            if (result == 1) {
                map.put("message", "success");
                return ResponseEntity.status(HttpStatus.OK).body(map);
            } else {
                map.put("message", "fail");
                return ResponseEntity.status(HttpStatus.OK).body(map);
            }
        } else{
            map.put("message", "tokenFail");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map);
        }
    }

    @ApiOperation(value= "피드백 페이지")
    @GetMapping(value = "/feedback")
    public ResponseEntity<Map<String, Object>> getFeedback(){
        Map<String, Object> map = new HashMap<>();
        map.put("FeedbackData",feedbackService.findFeedback());
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
}
