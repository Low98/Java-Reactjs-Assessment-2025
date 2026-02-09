package com.assessment.service;

import com.assessment.entity.User;
import com.assessment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User createUser(User user){
        return userRepository.save(user);
    }
}
