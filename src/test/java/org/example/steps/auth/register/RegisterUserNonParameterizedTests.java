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
        request = userGenerator.generateUser();
    }

    @After
    public void clean() {
        authStep.deleteUser(userGenerator.getAccessToken(request));
    }

    @Test
    @DisplayName("Registering valid user")
    public void registerValidUser() {
        Response response = authStep.sentPostToRegisterPath(request);
        authStep.checkStatusCode(response, SC_OK);
        authStep.checkSuccess(response, true);
        authStep.checkUser(response, request.getEmail(), request.getName());
        authStep.checkAccessToken(response);
        authStep.checkRefreshToken(response);
    }

    @Test
    @DisplayName("Registering an existing user")
    public void registerDuplicateUser() {
        authStep.registerUser(request);
        Response response = authStep.sentPostToRegisterPath(request);
        authStep.checkStatusCode(response, SC_FORBIDDEN);
        authStep.checkSuccess(response, false);
        authStep.checkMessage(response, DUPLICATE_USER_MESSAGE);
    }
}
