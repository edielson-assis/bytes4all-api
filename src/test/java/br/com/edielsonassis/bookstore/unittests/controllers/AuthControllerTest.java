package br.com.edielsonassis.bookstore.unittests.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.edielsonassis.bookstore.BookstoreApplication;
import br.com.edielsonassis.bookstore.controllers.AuthController;
import br.com.edielsonassis.bookstore.dtos.v1.request.UserSigninRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.UserSignupRequest;
import br.com.edielsonassis.bookstore.dtos.v1.response.TokenAndRefreshTokenResponse;
import br.com.edielsonassis.bookstore.dtos.v1.response.TokenResponse;
import br.com.edielsonassis.bookstore.dtos.v1.response.UserResponse;
import br.com.edielsonassis.bookstore.security.JwtTokenProvider;
import br.com.edielsonassis.bookstore.security.SecurityConfig;
import br.com.edielsonassis.bookstore.services.AuthService;
import br.com.edielsonassis.bookstore.unittests.mapper.mocks.MockUser;

@ContextConfiguration(classes = {BookstoreApplication.class, SecurityConfig.class, JwtTokenProvider.class})
@WebMvcTest(AuthController.class)
public class AuthControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService service;

    @MockBean
    private UserDetailsService usuarioDetailsService;

    @Mock
    private UserResponse userResponse;

    @Mock
    private TokenAndRefreshTokenResponse tokenAndRefreshTokenResponse;

    @Mock
    private TokenResponse tokenResponse;

    private MockUser input;
    private UserSignupRequest userSignupRequest;
    private UserSigninRequest userSigninRequest;

    private static final String BASE_PATH = "/api/v1/auth";
    private static final String USERNAME = "teste@email.com";
    private static final String REFRESH_TOKEN = "refreshToken";

    @BeforeEach
    void setup() {
        input = new MockUser();
        userSignupRequest = input.userSignup();
        userSigninRequest = input.userSignin();
    }

    @Test
    @DisplayName("Should signup and return a UserResponse")
	void testShouldSignupAndReturnUserResponse() throws JsonProcessingException, Exception {
        given(service.signup(any(UserSignupRequest.class))).willReturn(userResponse);

        ResultActions response = mockMvc.perform(post(BASE_PATH.concat("/signup")).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignupRequest)));

        response.andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName", is(userResponse.getFullName())))
                .andExpect(jsonPath("$.email", is(userResponse.getEmail())));
    }

    @Test
    @DisplayName("Should perform login and return a JWT token and a refresh token")
    void testShouldPerformLoginAndReturnAJwtTokenAndARefreshToken() throws JsonProcessingException, Exception {
        given(service.signin(any(UserSigninRequest.class))).willReturn(tokenAndRefreshTokenResponse);

        ResultActions response = mockMvc.perform(post(BASE_PATH.concat("/signin")).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSigninRequest)));

        response.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.accessToken", is(tokenAndRefreshTokenResponse.accessToken())))
                .andExpect(jsonPath("$.refreshToken", is(tokenAndRefreshTokenResponse.refreshToken())));
    }


    @Test
    @DisplayName("Should refresh a JWT token")
    void testShouldRefreshAJWTToken() throws Exception {
        given(service.refreshToken(anyString(), anyString())).willReturn(tokenResponse);

        ResultActions response = mockMvc.perform(get(BASE_PATH.concat("/refresh/{username}"), USERNAME)
                .header("Authorization", REFRESH_TOKEN));

        response.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.accessToken", is(tokenResponse.accessToken())));
                
    }

    @Test
    @WithMockUser
    @DisplayName("When delete user then return no content")
    void testWhenDeleteUserThenReturnNoContent() throws JsonProcessingException, Exception {
        willDoNothing().given(service).disableUser(USERNAME);

        ResultActions response = mockMvc.perform(delete(BASE_PATH.concat("/delete/{email}"), USERNAME));

        response.andExpect(status().isNoContent()).andDo(print());
    }
}