package org.example.rzah.ordermanagement.controller;

import com.jayway.jsonpath.JsonPath;
import org.example.rzah.ordermanagement.domain.UserEntity;
import org.example.rzah.ordermanagement.repository.UserRepository;
import org.example.rzah.ordermanagement.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class UserControllerIT {
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test_containers")
            .withUsername("test")
            .withPassword("Wtd#hggjur^^guJr");
    @Autowired
    private UserService userService;

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void createUser_withValidData_ShouldReturnCreated() throws Exception {
        String requestJson = createUserJson("newuser@gmail.com", "New User");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("newuser@gmail.com"));
    }

    @Test
    void createUser_withDuplicateEmail_ShouldReturnConflict() throws Exception {
        String requestJson = createUserJson("newuser@gmail.com", "New User");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isConflict());
    }

    @Test
    void getUserById_withValidData_ShouldReturnOneUser() throws Exception {
        String requestJson = createUserJson("newuser@gmail.com", "New User");

        // Create user
        MvcResult createResult = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseJson = createResult.getResponse().getContentAsString();

        Long userId = JsonPath.parse(responseJson).read("$.id", Long.class);

        mockMvc.perform(get(("/users/" + userId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.email").value("newuser@gmail.com"))
                .andExpect(jsonPath("$.name").value("New User"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void getUserById_withNotExistsId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get(("/users/999999"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUsers_withFirstPage_ShouldReturnUsers() throws Exception {
        final int page = 0;
        final int size = 2;

        List<Long> savedUserIds = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            UserEntity user = new UserEntity();
            user.setName("User " + i);
            user.setEmail("user" + i + "@gmail.com");
            user = userRepository.save(user);
            savedUserIds.add(user.getId());
        }

        MvcResult mvcResult = mockMvc.perform(get("/users?page=" + page + "&size=" + size))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users").exists())
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.users.length()").value(2))
                .andExpect(jsonPath("$.paginationInfo").exists())
                .andExpect(jsonPath("$.paginationInfo.size").value(size))
                .andExpect(jsonPath("$.paginationInfo.page").value(page))
                .andExpect(jsonPath("$.paginationInfo.totalPages").value(1))
                .andExpect(jsonPath("$.paginationInfo.totalElements").value(2))
                .andExpect(jsonPath("$.paginationInfo.hasNext").value(false))
                .andExpect(jsonPath("$.paginationInfo.hasPrevious").value(false))
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        List<Long> userIds = JsonPath.parse(responseJson)
                .read("$.users[*].id", List.class)
                .stream()
                .map(id -> ((Number) id).longValue())
                .toList();
        assertThat(userIds).containsExactlyInAnyOrder(savedUserIds.get(0), savedUserIds.get(1));
    }

    @Test
    void getUsers_withSecondPage_ShouldReturnUsers() throws Exception {
        final int page = 1;
        final int size = 2;
        final int totalElements = 3;

        for (int i = 0; i < totalElements; i++) {
            UserEntity user = new UserEntity();
            user.setName("User " + i);
            user.setEmail("user" + i + "@gmail.com");
            user = userRepository.save(user);
        }

        mockMvc.perform(get(String.format("/users?page=%s&size=%s", page, size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users").exists())
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.users.length()").value(1))
                .andExpect(jsonPath("$.paginationInfo").exists())
                .andExpect(jsonPath("$.paginationInfo.size").value(size))
                .andExpect(jsonPath("$.paginationInfo.page").value(page))
                .andExpect(jsonPath("$.paginationInfo.totalPages").value(2))
                .andExpect(jsonPath("$.paginationInfo.totalElements").value(totalElements))
                .andExpect(jsonPath("$.paginationInfo.hasNext").value(false))
                .andExpect(jsonPath("$.paginationInfo.hasPrevious").value(true));
    }

    private static String createUserJson(String email, String name) {
        return String.format("""
            {
                "email": "%s",
                "name": "%s"
            }
        """, email, name);
    }
}