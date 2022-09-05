package com.example.tamna.service;

import com.example.tamna.mapper.StaticDataMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Service
public class StaticDataService {
    private static StaticDataMapper staticDataMapper;
    public StaticDataService(StaticDataMapper staticDataMapper){this.staticDataMapper = staticDataMapper;}

    public static void userCsv() throws IOException {
//        String path = new ClassPathResource("/data/userData.csv").getFile().getAbsolutePath();
//        System.out.println(path); // 이것도 절대경로인듯.... 흐엉
        File csv = new File("/Users/m1naworld/eclipse-workspace/tamna/src/main/resources/data/userData.csv");
        BufferedReader br = new BufferedReader(new FileReader(csv));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
            String[] datalines = line.split(",");
            int classes = Integer.parseInt(datalines[0]);
            String userId = datalines[1];
            String name = datalines[2];
            staticDataMapper.insertUser(classes, userId, name);
        }
        br.close();
    } //userCsv() 메소드

    public static void conferenceCsv() throws IOException {
        File csv = new File("/Users/m1naworld/eclipse-workspace/tamna/src/main/resources/data/conferenceData.csv");
        BufferedReader br = new BufferedReader(new FileReader(csv));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
            String[] datalines = line.split(",");
            int floor = Integer.parseInt(datalines[0]);
            int conferRoomNum = Integer.parseInt(datalines[1]);
            String roomName = datalines[2];
            staticDataMapper.insertConfer(floor, conferRoomNum, roomName);
        }
        br.close();
    } //conferenceCsv() 메소드
}
