package com.kienluu.lab3embed.controller;

import com.kienluu.lab3embed.entity.UserEntity;
import com.kienluu.lab3embed.service.MobileAppService;
import com.kienluu.lab3embed.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final MobileAppService mobileAppService;

    @GetMapping("/user/all")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        try {
            List<UserEntity> users = userService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/user/new")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
        try {
            UserEntity saveUser = userService.saveUser(user);
            return new ResponseEntity<>(saveUser, HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<UserEntity> login(@RequestBody UserEntity user) {
        try {
            UserEntity userEntity = userService.loginUser(user);
            return new ResponseEntity<>(userEntity, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserEntity> getUser(@PathVariable Long userId) {
        try{
            UserEntity user = userService.getUserById(userId);
            mobileAppService.sendMessage(userId, user.getMessage());
            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Long userId, @RequestBody UserEntity user) {
        try{
            UserEntity updateUser = userService.updateUserMessage(userId,user);
            mobileAppService.sendMessage(userId, user.getMessage());
            return new ResponseEntity<>(updateUser, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
