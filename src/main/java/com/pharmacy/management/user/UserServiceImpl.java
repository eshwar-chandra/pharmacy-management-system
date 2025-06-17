package com.pharmacy.management.user; // Updated package

// Updated imports to reflect new package structure for exceptions and User-specific classes
import com.pharmacy.management.common.exception.ResourceNotFoundException;
import com.pharmacy.management.user.UserDTO;
import com.pharmacy.management.user.UserMapper;
import com.pharmacy.management.user.User;
import com.pharmacy.management.user.UserRepository;
import com.pharmacy.management.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
// Removed java.util.Optional as it's not directly used after refactoring to orElseThrow
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Cacheable(value = "userCache", key = "#id")
    public UserDTO getUserById(Long id) {
        log.info("Fetching user with ID: {}", id);
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
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
    @CachePut(value = "userCache", key = "#id")
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        log.info("Updating user with ID {}: {}", id, userDTO.getUsername());
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            // In a real app, hash password: existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            // For now, setting plain text password if provided
            existingUser.setPassword(userDTO.getPassword());
        }
        // Timestamps managed by @PreUpdate in User entity
        return userMapper.toDTO(userRepository.save(existingUser));
    }

    @Override
    @Transactional
    @CacheEvict(value = "userCache", key = "#id", allEntries = false)
    public void deleteUser(Long id) {
        log.info("Soft deleting user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        user.setSoftDelete(true);
        userRepository.save(user);
    }
}
