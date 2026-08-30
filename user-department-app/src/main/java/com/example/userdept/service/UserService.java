package com.example.userdept.service;

import com.example.userdept.dto.CreateUserRequest;
import com.example.userdept.entity.User;
import com.example.userdept.exception.ResourceNotFoundException;
import com.example.userdept.repository.DepartmentRepository;
import com.example.userdept.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public UserService(UserRepository userRepository, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    // Return all users from the database.
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Fetch a single user by ID or throw a custom exception if it does not exist.
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    // Create a new user only if the department ID is valid.
    @Transactional
    public User createUser(CreateUserRequest request) {
        // Ensure the provided department exists before creating a user record.
        departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with id: " + request.getDepartmentId()));

        // Create the user with the requested name and department reference.
        User user = new User(request.getName(), request.getDepartmentId());
        return userRepository.save(user);
    }

    // Toggle the user's active state and save the updated record.
    @Transactional
    public User toggleActive(Long id) {
        User user = getUserById(id);
        user.setActive(!user.isActive());
        return userRepository.save(user);
    }
}
