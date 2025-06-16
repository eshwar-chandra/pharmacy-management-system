package com.pharmacy.management.services.impl;

import com.pharmacy.management.dto.UserDTO;
import com.pharmacy.management.mapper.UserMapper;
import com.pharmacy.management.models.User;
import com.pharmacy.management.repositories.UserRepository;
import com.pharmacy.management.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO getUserById(Long id) {
        log.info("Fetching user with ID: {}", id);
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.map(userMapper::toDTO).orElse(null);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO addUser(UserDTO userDTO) {
        log.info("Adding new user: {}", userDTO.getUsername());
        User user = userMapper.toEntity(userDTO);
        // In a real app, hash password here before saving: user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        log.info("Updating user with ID {}: {}", id, userDTO.getUsername());
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setUsername(userDTO.getUsername());
            existingUser.setEmail(userDTO.getEmail());
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                // In a real app, hash password: existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                existingUser.setPassword(userDTO.getPassword());
            }
            // Timestamps managed by @PreUpdate
            return userMapper.toDTO(userRepository.save(existingUser));
        } else {
            log.warn("User with ID {} not found for update", id);
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Soft deleting user with ID: {}", id);
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setSoftDelete(true);
            userRepository.save(user);
        } else {
            log.warn("User with ID {} not found for deletion", id);
        }
    }
}
