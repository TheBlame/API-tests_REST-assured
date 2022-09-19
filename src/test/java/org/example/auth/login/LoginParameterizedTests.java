package org.example.auth.login;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.pojo.auth.AuthRequest;
import org.example.AbstractTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.*;

@RunWith(Parameterized.class)
public class LoginParameterizedTests extends AbstractTest {
    private static final String INVALID_REQUEST_MESSAGE = "email or password are incorrect";
    private static final AuthRequest user = userGenerator.generateUser();

    private final AuthRequest request;

    public LoginParameterizedTests(String message, AuthRequest request) {
        this.request = request;
    }

    @Parameterized.Parameters(name = "Test data: {0}")
    public static Object[][] getTestData() {
        return new Object[][] {
                {"Login with wrong email", userGenerator.generateCredentialsWithWrongEmail(user)},
                {"Login with wrong password", userGenerator.generateCredentialsWithWrongPassword(user)}
        };
    }

    @BeforeClass
    public static void setUp() {
        authStep.registerUser(user);
    }

    @AfterClass
    public static void clean() {
        authStep.deleteUser(userGenerator.getAccessToken(user));
    }

    @Test
    @DisplayName("Login with invalid credentials")
    public void loginWithInvalidCredentials() {
        Response response = authStep.sentPostToLoginPath(request);
        authStep.checkStatusCode(response, SC_UNAUTHORIZED);
        authStep.checkSuccess(response, false);
        authStep.checkMessage(response, INVALID_REQUEST_MESSAGE);
    }
}
