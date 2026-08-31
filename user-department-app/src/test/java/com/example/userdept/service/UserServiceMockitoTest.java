package com.example.userdept.service;

import com.example.userdept.dto.CreateUserRequest;
import com.example.userdept.entity.Department;
import com.example.userdept.entity.User;
import com.example.userdept.exception.ResourceNotFoundException;
import com.example.userdept.repository.DepartmentRepository;
import com.example.userdept.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceMockitoTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_whenDepartmentExists_savesUser() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Alice");
        request.setDepartmentId(200L);

        Department department = new Department(200L, "Engineering");
        when(departmentRepository.findById(200L)).thenReturn(Optional.of(department));

        User savedUser = new User("Alice", 200L);
        savedUser.setId(1L);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.createUser(request);

        assertNotNull(result);
        assertEquals("Alice", result.getName());
        assertEquals(200L, result.getDepartmentId());
        assertTrue(result.isActive());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_whenDepartmentDoesNotExist_throwsException() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Bob");
        request.setDepartmentId(999L);

        when(departmentRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.createUser(request)
        );

        assertTrue(ex.getMessage().contains("Department not found with id: 999"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void toggleActive_flipsUserStatus() {
        User user = new User("Charlie", 100L);
        user.setId(5L);
        user.setActive(true);

        when(userRepository.findById(5L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.toggleActive(5L);

        assertFalse(result.isActive());
        verify(userRepository).save(user);
    }
}
