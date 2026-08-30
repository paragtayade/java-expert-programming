package com.example.userdept;

import com.example.userdept.dto.CreateUserRequest;
import com.example.userdept.entity.User;
import com.example.userdept.repository.DepartmentRepository;
import com.example.userdept.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserDepartmentApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    // AC1: fetch all departments
    @Test
    void getAllDepartments_returnsFourSeededDepartments() throws Exception {
        mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(4)))
                .andExpect(jsonPath("$[?(@.id == 100)].name", hasItem("Administration")))
                .andExpect(jsonPath("$[?(@.id == 200)].name", hasItem("Engineering")));
    }

    // AC2: fetch all users
    @Test
    void getAllUsers_returnsEmptyInitially() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    // AC3: fetch user by ID
    @Test
    void getUserById_returnsUser() throws Exception {
        User saved = userRepository.save(new User("Alice", 200L));

        mockMvc.perform(get("/users/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Alice")))
                .andExpect(jsonPath("$.departmentId", is(200)))
                .andExpect(jsonPath("$.active", is(true)));
    }

    @Test
    void getUserById_returns404WhenNotFound() throws Exception {
        mockMvc.perform(get("/users/9999"))
                .andExpect(status().isNotFound());
    }

    // AC4: toggle user active/inactive
    @Test
    void toggleActive_togglesUserStatus() throws Exception {
        User saved = userRepository.save(new User("Bob", 100L));

        mockMvc.perform(patch("/users/" + saved.getId() + "/toggle-active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active", is(false)));

        mockMvc.perform(patch("/users/" + saved.getId() + "/toggle-active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active", is(true)));
    }

    // AC5: fetch department by ID with list of active user names
    @Test
    void getDepartmentById_returnsActiveUserNames() throws Exception {
        User active = userRepository.save(new User("Charlie", 200L));
        User inactive = userRepository.save(new User("Dave", 200L));
        inactive.setActive(false);
        userRepository.save(inactive);

        mockMvc.perform(get("/departments/200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(200)))
                .andExpect(jsonPath("$.name", is("Engineering")))
                .andExpect(jsonPath("$.activeUserNames", hasSize(1)))
                .andExpect(jsonPath("$.activeUserNames[0]", is("Charlie")));
    }

    @Test
    void getDepartmentById_returns404WhenNotFound() throws Exception {
        mockMvc.perform(get("/departments/9999"))
                .andExpect(status().isNotFound());
    }

    // Create user endpoint
    @Test
    void createUser_returns201WithCreatedUser() throws Exception {
        CreateUserRequest req = new CreateUserRequest();
        req.setName("Eve");
        req.setDepartmentId(300L);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Eve")))
                .andExpect(jsonPath("$.departmentId", is(300)))
                .andExpect(jsonPath("$.active", is(true)));
    }

    @Test
    void createUser_returns404WhenDepartmentNotFound() throws Exception {
        CreateUserRequest req = new CreateUserRequest();
        req.setName("Frank");
        req.setDepartmentId(9999L);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }
}
