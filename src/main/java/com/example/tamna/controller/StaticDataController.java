package com.example.tamna.controller;

import com.example.tamna.service.StaticDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/csv")
public class StaticDataController {
    public final StaticDataService staticDataService;

//    @GetMapping(value = "/user")
    @PostMapping(value = "/user")
    public String insertUserData(@RequestPart(required = false) MultipartFile file) throws IOException {
        File converFile = new File(file.getOriginalFilename());
        file.transferTo(converFile);
        System.out.println(converFile);

        System.out.println("여기왔냐");
        System.out.println("32r24254 2525  : " + file);

        BufferedReader br = new BufferedReader((Reader) file);
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
            String[] datalines = line.split(",");
            int classes = Integer.parseInt(datalines[0]);
            String userId = datalines[1];
            String name = datalines[2];
            System.out.println(classes +  " " + userId + " " + name);
//            staticDataMapper.insertUser(classes, userId, name);
        }
        br.close();
//        StaticDataService.userCsv();
        return "success";
    }

//
//    @PostMapping(value = "/addUser")
//    public String updateUserFile(@RequestParam("file") MultipartFile file) throws IOException{
//        System.out.println();
//        String fileName = staticDataService.storeFile(file);
//
//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/downloadFile/")
//                .path(fileName)
//                .toUriString();
//
//        System.out.println(fileDownloadUri);
//        return new FileUploadDTO(fileName, fileDownloadUri,
//                file.getContentType(), file.getSize());
//    }


    @GetMapping(value = "/conference-room")
    public String insertConferenceRoom() throws IOException {
        StaticDataService.conferenceCsv();
        return "success";
    }
}
