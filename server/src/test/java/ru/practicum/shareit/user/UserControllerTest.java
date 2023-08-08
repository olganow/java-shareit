package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    private UserDto userDto;

    @BeforeEach
    void beforeEach() {
        userDto = UserDto.builder()
                .id(1L)
                .name("User_Name")
                .email("user@test.testz")
                .build();
    }

    @Test
    public void createUserTest() throws Exception {
        when(userService.createUser(any())).thenReturn(userDto);
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    public void getUserByIdTest() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(userDto);
        mockMvc.perform(get("/users/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    public void getUserByWrongIdValidationTest() throws Exception {
        when(userService.getUserById(anyLong())).thenThrow(new NotFoundException("User has not been found"));
        mockMvc.perform(get("/users/{userId}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllUsersTest() throws Exception {
        List<UserDto> users = new ArrayList<>();
        users.add(userDto);
        when(userService.getAllUsers()).thenReturn(users);
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(userDto.getName())))
                .andExpect(jsonPath("$[0].email", is(userDto.getEmail())));
    }

    @Test
    public void updateUserByIdTest() throws Exception {
        userDto.setName("UpdatedName");
        userDto.setEmail("updatedmail@mail.ru");
        when(userService.updateUserById(anyLong(), any())).thenReturn(userDto);
        mockMvc.perform(patch("/users/{userId}", 1L)
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    void deleteById() throws Exception {
        Long userId = 1L;
        mockMvc.perform(delete("/users/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).removeUserById(userId);
    }
}
