package org.example.steps.auth.register;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.pojo.auth.AuthRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.example.helpers.UserGenerator.*;
import static org.example.helpers.Utils.*;
import static org.example.steps.steps.AuthSteps.*;

public class RegisterUserNonParameterizedTests {
    private static final String BASE_PATH = "/api/auth/register";
    private static final String DUPLICATE_USER_MESSAGE = "User already exists";

    private AuthRequest request;

    @Before
    public void setUp() {
        request = generateUser();
    }

    @After
    public void clean() {
        deleteUser(getAccessToken(request));
    }

    @Test
    @DisplayName("Registering valid user")
    public void registerValidUser() {
        Response response = sendPostRequest(request, BASE_PATH);
        checkStatusCode(response, SC_OK);
        checkSuccess(response, true);
        checkUser(response, request.getEmail(), request.getName());
        checkAccessToken(response);
        checkRefreshToken(response);
    }

    @Test
    @DisplayName("Registering an existing user")
    public void registerDuplicateUser() {
        registerUser(request);
        Response response = sendPostRequest(request, BASE_PATH);
        checkStatusCode(response, SC_FORBIDDEN);
        checkSuccess(response, false);
        checkMessage(response, DUPLICATE_USER_MESSAGE);
    }
}
