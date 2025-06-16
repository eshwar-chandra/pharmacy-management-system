package com.pharmacy.management.services.impl;

import com.pharmacy.management.dto.UserDTO;
import com.pharmacy.management.mapper.UserMapper;
import com.pharmacy.management.models.User;
import com.pharmacy.management.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setUsername("testuser");
        user.setSoftDelete(false);
        user.setPassword("password"); // In real tests, this would be hashed

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuserDTO");
        userDTO.setPassword("password");
    }

    @Test
    void getUserById_found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("testuserDTO", result.getUsername());
        verify(userRepository).findById(1L);
        verify(userMapper).toDTO(user);
    }

    @Test
    void getUserById_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        UserDTO result = userService.getUserById(1L);
        assertNull(result);
        verify(userRepository).findById(1L);
    }

    @Test
    void getAllUsers_success() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        List<UserDTO> results = userService.getAllUsers();

        assertNotNull(results);
        assertEquals(1, results.size());
        verify(userRepository).findAll();
    }

    @Test
    void getAllUsers_empty() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        List<UserDTO> results = userService.getAllUsers();
        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(userRepository).findAll();
    }

    @Test
    void addUser_success() {
        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.addUser(userDTO);

        assertNotNull(result);
        assertEquals("testuserDTO", result.getUsername());
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_found() {
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setUsername("updatedUser");
        updatedUserDTO.setEmail("updated@example.com");
        // Password update is conditional in service, so test that path if needed
        // For this test, assume password is not being updated to simplify
        updatedUserDTO.setPassword(null);


        User existingUser = new User(); // Simulate existing user
        existingUser.setUserId(1L);
        existingUser.setUsername("originalUser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // return saved entity
        when(userMapper.toDTO(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            UserDTO dto = new UserDTO();
            dto.setId(savedUser.getUserId());
            dto.setUsername(savedUser.getUsername());
            dto.setEmail(savedUser.getEmail());
            return dto;
        });


        UserDTO result = userService.updateUser(1L, updatedUserDTO);

        assertNotNull(result);
        assertEquals("updatedUser", result.getUsername());
        assertEquals("updated@example.com", result.getEmail());
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }


    @Test
    void updateUser_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        UserDTO result = userService.updateUser(1L, userDTO);
        assertNull(result);
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_softDeleteSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user); // Mock save for soft delete

        userService.deleteUser(1L);

        assertTrue(user.isSoftDelete());
        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
    }

    @Test
    void deleteUser_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        userService.deleteUser(1L);
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any());
    }
}
