package com.example.learning_platform.service;

import com.example.learning_platform.entity.Role;
import com.example.learning_platform.entity.User;
import com.example.learning_platform.entity.UserRepository;
import com.example.learning_platform.exception.BusinessException;
import com.example.learning_platform.exception.NotFoundException;
import com.example.learning_platform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User createUser(String name, String email, Role role) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException("Email уже занят: " + email);
        }
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setRole(role);
        return userRepository.save(u);
    }

    @Transactional(readOnly = true)
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + id));
    }

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepository.findAll();
    }
}