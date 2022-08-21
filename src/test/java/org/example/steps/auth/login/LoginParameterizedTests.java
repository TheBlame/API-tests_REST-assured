package org.example.steps.auth.login;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.pojo.auth.AuthRequest;
import org.example.steps.AbstractTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.*;

@RunWith(Parameterized.class)
public class LoginParameterizedTests extends AbstractTest {
    private static final String INVALID_REQUEST_MESSAGE = "email or password are incorrect";
    private static final AuthRequest USER = USER_GENERATOR.generateUser();

    private final AuthRequest request;

    public LoginParameterizedTests(String message, AuthRequest request) {
        this.request = request;
    }

    @Parameterized.Parameters(name = "Test data: {0}")
    public static Object[][] getTestData() {
        return new Object[][] {
                {"Login with wrong email", USER_GENERATOR.generateCredentialsWithWrongEmail(USER)},
                {"Login with wrong password", USER_GENERATOR.generateCredentialsWithWrongPassword(USER)}
        };
    }

    @BeforeClass
    public static void setUp() {
        UTILS.registerUser(USER);
    }

    @AfterClass
    public static void clean() {
        UTILS.deleteUser(UTILS.getAccessToken(USER));
    }

    @Test
    @DisplayName("Login with invalid credentials")
    public void loginWithInvalidCredentials() {
        Response response = AUTH_STEP.sentPostToLoginPath(request);
        AUTH_STEP.checkStatusCode(response, SC_UNAUTHORIZED);
        AUTH_STEP.checkSuccess(response, false);
        AUTH_STEP.checkMessage(response, INVALID_REQUEST_MESSAGE);
    }
}
