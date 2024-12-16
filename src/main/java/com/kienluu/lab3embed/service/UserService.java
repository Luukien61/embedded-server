package com.kienluu.lab3embed.service;

import com.kienluu.lab3embed.entity.UserEntity;
import com.kienluu.lab3embed.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    public UserEntity loginUser(UserEntity user) {
        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword())
                .orElseThrow(() -> new UsernameNotFoundException("Username or password does not exist"));
    }

    public UserEntity updateUserMessage(Long userId, UserEntity user) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userEntity.setMessage(user.getMessage());
        return userRepository.save(userEntity);
    }

}
