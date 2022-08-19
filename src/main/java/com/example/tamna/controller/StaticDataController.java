package com.example.tamna.controller;

import com.example.tamna.service.StaticDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/csv")
public class StaticDataController {

    @GetMapping(value = "/user")
    public String insertUserData() throws IOException {
        StaticDataService.userCsv();
        return "success";
    }

    @GetMapping(value = "/conference-room")
    public String insertConferenceRoom() throws IOException {
        StaticDataService.conferenceCsv();
        return "success";
    }
}
