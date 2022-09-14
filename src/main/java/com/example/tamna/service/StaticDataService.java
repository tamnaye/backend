package com.example.tamna.service;

import com.example.tamna.mapper.StaticDataMapper;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StaticDataService {
    private static StaticDataMapper staticDataMapper;
//    private final Environment env;
//    private final Path fileLocaion;

    @Autowired
    public StaticDataService(StaticDataMapper staticDataMapper) throws IOException {
        this.staticDataMapper = staticDataMapper;
//        this.env = env;
//        this.fileLocaion = Paths.get(env.getProperty("file.uploadDir")).toAbsolutePath().normalize();
//
//        Files.createDirectories(this.fileLocaion);


    }

//    public String storeFile(MultipartFile file) throws IOException {
//        // cleanPath : 역슬래시를 /슬래시로 바꿔줌
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//
//            if (fileName.contains("..")) {
//            // 저장할 fileStorageLocation 경로 뒤에 파일명을 붙여준다. (경로 조합)
//            Path targetLocation = this.fileLocaion.resolve(fileName);
//            //업로드할 file을 targetLocation에 복사한다. (동일한 이름일 경우 replace)
//            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//            return fileName;
//        }
//    }


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
