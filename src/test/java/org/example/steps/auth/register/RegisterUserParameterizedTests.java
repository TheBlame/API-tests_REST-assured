package org.example.steps.auth.register;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.pojo.auth.AuthRequest;
import org.example.steps.AbstractTest;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.*;

@RunWith(Parameterized.class)
public class RegisterUserParameterizedTests extends AbstractTest {
    private static final String INVALID_REQUEST_MESSAGE = "Email, password and name are required fields";

    private final AuthRequest request;

    public RegisterUserParameterizedTests(String message, AuthRequest request) {
        this.request = request;
    }

    @Parameterized.Parameters(name = "Test data: {0}")
    public static Object[][] getTestData() {
        return new Object[][]{
                {"User without name", USER_GENERATOR.generateUserWithoutName()},
                {"User without password", USER_GENERATOR.generateUserWithoutPassword()},
                {"User without email", USER_GENERATOR.generateUserWithoutEmail()}
        };
    }

    @After
    public void clean() {
        UTILS.deleteUser(UTILS.getAccessToken(request));
    }

    @Test
    @DisplayName("Register user with invalid request")
    public void registerUserWithInvalidRequest() {
        Response response = AUTH_STEP.sentPostToRegisterPath(request);
        AUTH_STEP.checkStatusCode(response, SC_FORBIDDEN);
        AUTH_STEP.checkSuccess(response, false);
        AUTH_STEP.checkMessage(response, INVALID_REQUEST_MESSAGE);
    }
}
