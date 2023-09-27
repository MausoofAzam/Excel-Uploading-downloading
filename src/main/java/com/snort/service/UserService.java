package com.snort.service;

import com.snort.entity.User;
import com.snort.helper.ExcelHelper;
import com.snort.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    //saving the list of data in db.
    public void save(MultipartFile file) {
        try {
            List<User> list = ExcelHelper.convertExcelToListOfUser(file.getInputStream());
            this.userRepository.saveAll(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //getting the list of users from db
    public List<User> getAllUser() {
        return userRepository.findAll();
    }


    public User getUserById(Integer userId){
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()){
            return null;
        }else {
         return    userOptional.get();
        }
    }

    public Integer saveUser(User user) {

        User savedUser = userRepository.save(user);
        return savedUser.getUserId();
    }
}
