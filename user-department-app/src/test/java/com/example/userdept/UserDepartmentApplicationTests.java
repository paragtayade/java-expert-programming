package com.example.userdept;

import com.example.userdept.dto.CreateUserRequest;
import com.example.userdept.dto.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserDepartmentApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void ac1_getAllDepartments_returnsFourDepartments() throws Exception {
        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].name").value("Administration"));
    }

    @Test
    void ac2_getAllUsers_returnsEmptyInitially() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void ac3_getUserById_returnsUser() throws Exception {
        // Create a user first
        CreateUserRequest request = new CreateUserRequest("Alice", 100);
        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        UserResponse created = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);

        mockMvc.perform(get("/api/users/" + created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.departmentId").value(100))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void ac4_toggleUserActive_togglesStatus() throws Exception {
        CreateUserRequest request = new CreateUserRequest("Bob", 200);
        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        UserResponse created = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponse.class);

        // Toggle to inactive
        mockMvc.perform(patch("/api/users/" + created.id() + "/toggle-active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));

        // Toggle back to active
        mockMvc.perform(patch("/api/users/" + created.id() + "/toggle-active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void ac5_getDepartmentById_includesActiveUserNames() throws Exception {
        // Add an active user to dept 300
        CreateUserRequest active = new CreateUserRequest("Carol", 300);
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(active)))
                .andExpect(status().isCreated());

        // Add user then deactivate
        CreateUserRequest inactive = new CreateUserRequest("Dave", 300);
        MvcResult res = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inactive)))
                .andExpect(status().isCreated())
                .andReturn();
        UserResponse dave = objectMapper.readValue(res.getResponse().getContentAsString(), UserResponse.class);
        mockMvc.perform(patch("/api/users/" + dave.id() + "/toggle-active"));

        mockMvc.perform(get("/api/departments/300"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(300))
                .andExpect(jsonPath("$.name").value("Finance"))
                .andExpect(jsonPath("$.activeUserNames", hasItem("Carol")))
                .andExpect(jsonPath("$.activeUserNames", not(hasItem("Dave"))));
    }

    @Test
    void createUser_withInvalidDepartment_returns400() throws Exception {
        CreateUserRequest request = new CreateUserRequest("Unknown", 999);
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDepartmentById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/departments/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/users/99999"))
                .andExpect(status().isNotFound());
    }
}
