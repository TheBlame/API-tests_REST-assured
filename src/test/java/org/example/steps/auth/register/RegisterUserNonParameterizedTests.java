package org.example.steps.auth.register;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.pojo.auth.AuthRequest;
import org.example.steps.AbstractTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class RegisterUserNonParameterizedTests extends AbstractTest {
    private static final String DUPLICATE_USER_MESSAGE = "User already exists";
    private AuthRequest request;

    @Before
    public void setUp() {
        request = USER_GENERATOR.generateUser();
    }

    @After
    public void clean() {
        UTILS.deleteUser(UTILS.getAccessToken(request));
    }

    @Test
    @DisplayName("Registering valid user")
    public void registerValidUser() {
        Response response = AUTH_STEP.sentPostToRegisterPath(request);
        AUTH_STEP.checkStatusCode(response, SC_OK);
        AUTH_STEP.checkSuccess(response, true);
        AUTH_STEP.checkUser(response, request.getEmail(), request.getName());
        AUTH_STEP.checkAccessToken(response);
        AUTH_STEP.checkRefreshToken(response);
    }

    @Test
    @DisplayName("Registering an existing user")
    public void registerDuplicateUser() {
        UTILS.registerUser(request);
        Response response = AUTH_STEP.sentPostToRegisterPath(request);
        AUTH_STEP.checkStatusCode(response, SC_FORBIDDEN);
        AUTH_STEP.checkSuccess(response, false);
        AUTH_STEP.checkMessage(response, DUPLICATE_USER_MESSAGE);
    }
}
