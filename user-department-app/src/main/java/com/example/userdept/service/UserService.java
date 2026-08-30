package com.example.userdept.service;

import com.example.userdept.dto.CreateUserRequest;
import com.example.userdept.dto.UserResponse;
import com.example.userdept.model.User;
import com.example.userdept.repository.DepartmentRepository;
import com.example.userdept.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public UserService(UserRepository userRepository, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse getUserById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + id));
        return toResponse(user);
    }

    public UserResponse createUser(CreateUserRequest request) {
        if (!departmentRepository.existsById(request.departmentId())) {
            throw new IllegalArgumentException("Department not found: " + request.departmentId());
        }
        User user = new User(request.name(), request.departmentId());
        return toResponse(userRepository.save(user));
    }

    public UserResponse toggleUserActive(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + id));
        user.setActive(!user.isActive());
        userRepository.save(user);
        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getDepartmentId(), user.isActive());
    }
}
