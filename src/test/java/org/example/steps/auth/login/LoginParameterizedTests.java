package org.example.steps.auth.login;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.pojo.auth.AuthRequest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.example.helpers.UserGenerator.*;
import static org.example.helpers.Utils.*;
import static org.example.steps.steps.AuthSteps.*;

@RunWith(Parameterized.class)
public class LoginParameterizedTests {
    private static final String BASE_PATH = "/api/auth/login";
    private static final String INVALID_REQUEST_MESSAGE = "email or password are incorrect";
    private static final AuthRequest USER = generateUser();

    private final AuthRequest request;

    public LoginParameterizedTests(String message, AuthRequest request) {
        this.request = request;
    }

    @Parameterized.Parameters(name = "Test data: {0}")
    public static Object[][] getTestData() {
        return new Object[][] {
                {"Login with wrong email", generateCredentialsWithWrongEmail(USER)},
                {"Login with wrong password", generateCredentialsWithWrongPassword(USER)}
        };
    }

    @BeforeClass
    public static void setUp() {
        registerUser(USER);
    }

    @AfterClass
    public static void clean() {
        deleteUser(getAccessToken(USER));
    }

    @Test
    @DisplayName("Login with invalid credentials")
    public void loginWithInvalidCredentials() {
        Response response = sendPostRequest(request, BASE_PATH);
        checkStatusCode(response, SC_UNAUTHORIZED);
        checkSuccess(response, false);
        checkMessage(response, INVALID_REQUEST_MESSAGE);
    }
}
