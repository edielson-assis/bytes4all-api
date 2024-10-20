package br.com.edielsonassis.bookstore.unittests.mapper.mocks;

import br.com.edielsonassis.bookstore.dtos.v1.request.UserSigninRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.UserSignupRequest;
import br.com.edielsonassis.bookstore.dtos.v1.response.UserResponse;
import br.com.edielsonassis.bookstore.model.User;

public class MockUser {

    private static final Long USER_ID = 1L;
    
    public User user() {
        User user = new User();
        user.setUserId(USER_ID);
        user.setFullName("Test auth");
        user.setEmail("teste@email.com");
        user.setPassword("91e2532173dc95ef503ed5ed39f7822f576a93b7c5ae41ef52b2467bd0234f089bbfd3f3f79ed7ba");
        return user;
    }

    public UserSignupRequest userSignup() {
        UserSignupRequest user = new UserSignupRequest();
        user.setFullName("Test auth");
        user.setEmail("teste@email.com");
        user.setPassword("1234567");
        return user;
    }

    public UserSigninRequest userSignin() {
        UserSigninRequest user = new UserSigninRequest();
        user.setEmail("teste@email.com");
        user.setPassword("1234567");
        return user;
    }

    public UserResponse userResponse() {
        UserResponse user = new UserResponse();
        user.setUserId(USER_ID);
        user.setFullName("Test auth");
        user.setEmail("teste@email.com");
        return user;
    }
}