package app.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.controller.UserController;
import app.datastore.UserRepository;
import app.model.User;
import app.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private UserRepository userRepository;
    @MockBean private UserService userService;
    @Autowired private UserController controller;
    private JacksonTester < User > jsonTester;
    private User user;
    
    @Before
    public void setup() {
        JacksonTester.initFields(this, objectMapper);
        user = new User();
    }
    
    @Test
    public void contexLoads() throws Exception {
        assertThat(controller).isNotNull();
    }
    
    @Test
    public void addUser_IsValid_UserPersisted() throws Exception {
        final String userJson = jsonTester.write(user).getJson();
        given(userService.isUserValid(any(User.class))).willReturn(true);
        mockMvc
            .perform(post("/app/addUser").content(userJson).contentType(APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
    }
    
    @Test
    public void addUser_IsNotValid_UserNotPersisted() throws Exception {
        final String userJson = jsonTester.write(user).getJson();
        given(userService.isUserValid(any(User.class))).willReturn(false);
        mockMvc
            .perform(post("/app/addUser").content(userJson).contentType(APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest());
    }	
	
}