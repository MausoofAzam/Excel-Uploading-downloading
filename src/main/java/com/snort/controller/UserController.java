package com.snort.controller;

import com.snort.entity.User;
import com.snort.helper.ExcelHelper;
import com.snort.service.ExcelService;
import com.snort.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ExcelService excelService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        if (ExcelHelper.checkExcelFormat(file)) {
            this.userService.save(file);
            return ResponseEntity.ok(Map.of("message", "File is uploaded and data is saved to database."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload excel file");
        }
    }

    @GetMapping("/download-data")
    public ResponseEntity <byte[]> downloadDecryptedExcelData() throws IOException {
        List<User> users = userService.getAllUser();
        byte[] excelBytes = excelService.generateExcel(users);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "List_Users.xlsx");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return ResponseEntity.ok().headers(headers).body(excelBytes);
    }
}
