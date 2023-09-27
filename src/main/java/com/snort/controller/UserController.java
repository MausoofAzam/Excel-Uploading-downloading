package com.snort.controller;

import com.snort.entity.User;
import com.snort.helper.EncryptionHelper;
import com.snort.helper.ExcelHelper;
import com.snort.response.UserRequest;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    public ResponseEntity<byte[]> downloadDecryptedExcelData() throws IOException {
        List<User> users = userService.getAllUser();
        byte[] excelBytes = excelService.generateExcel(users);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "List_Users.xlsx");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return ResponseEntity.ok().headers(headers).body(excelBytes);
    }


    @GetMapping
    public List<UserRequest> getAllDecryptedUser() {
        List<User> encryptedUser = userService.getAllUser();
        List<UserRequest> userRequests = new ArrayList<>();

        for (User user : encryptedUser) {
            UserRequest userRequest = new UserRequest(
                    user.getUserId(),
                    EncryptionHelper.decrypt(user.getFirstName()),
                    EncryptionHelper.decrypt(user.getLastName()),
                    EncryptionHelper.decrypt(user.getEmail()),
                    EncryptionHelper.decrypt(user.getDesignation())
            );
            userRequests.add(userRequest);

        }
        return userRequests;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Integer userId) {
        try {
            User user = userService.getUserById(userId);
            UserRequest userRequest = new UserRequest(
                    user.getUserId(),
                    EncryptionHelper.decrypt(user.getFirstName()),
                    EncryptionHelper.decrypt(user.getLastName()),
                    EncryptionHelper.decrypt(user.getEmail()),
                    EncryptionHelper.decrypt(user.getDesignation())
            );
            return new ResponseEntity(userRequest, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("User id not found: " + userId, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<?> createEncryptedUser(@RequestBody UserRequest userRequest) {
        try {
            //encrypt the upcoming data
            String encryptedFirstName = EncryptionHelper.encrypt(userRequest.getFirstName());
            String encryptedLastName = EncryptionHelper.encrypt(userRequest.getLastName());
            String encryptedEmail = EncryptionHelper.encrypt(userRequest.getEmail());
            String encryptedDesignation = EncryptionHelper.encrypt(userRequest.getDesignation());

            //creating new user with encrypted data
            User user = new User();
            user.setUserId(generateRandomUserId());
            user.setFirstName(encryptedFirstName);
            user.setLastName(encryptedLastName);
            user.setEmail(encryptedEmail);
            user.setDesignation(encryptedDesignation);

            userService.saveUser(user);
            return new ResponseEntity("saved successfully in Encrypted form", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Unable to save the Data in the DB", HttpStatus.BAD_REQUEST);
        }
    }

    private Integer generateRandomUserId() {
        Random random = new Random();
        return random.nextInt(1000) + 1;
    }
}
