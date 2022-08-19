package org.example.steps.auth.register;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.pojo.auth.AuthRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.example.helpers.UserGenerator.*;
import static org.example.steps.steps.AuthSteps.*;

@RunWith(Parameterized.class)
public class RegisterUserParameterizedTests {
    private static final String BASE_PATH = "/api/auth/register";
    private static final String INVALID_REQUEST_MESSAGE = "Email, password and name are required fields";

    private final AuthRequest request;

    public RegisterUserParameterizedTests(String message, AuthRequest request) {
        this.request = request;
    }

    @Parameterized.Parameters(name = "Test data: {0}")
    public static Object[][] getTestData() {
        return new Object[][]{
                {"User without name", generateUserWithoutName()},
                {"User without password", generateUserWithoutPassword()},
                {"User without email", generateUserWithoutEmail()}
        };
    }

    @Test
    @DisplayName("Register user with invalid request")
    public void registerUserWithInvalidRequest() {
        Response response = sendPostRequest(request, BASE_PATH);
        checkStatusCode(response, SC_FORBIDDEN);
        checkSuccess(response, false);
        checkMessage(response, INVALID_REQUEST_MESSAGE);
    }
}
