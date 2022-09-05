package org.example.steps.auth.login;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.pojo.auth.AuthRequest;
import org.example.steps.AbstractTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class LoginNonParameterizedTests extends AbstractTest {
    private static AuthRequest user;

    @BeforeClass
    public static void setUp() {
        user = userGenerator.generateUser();
        authStep.registerUser(user);
    }

    @AfterClass
    public static void clean() {
        authStep.deleteUser(userGenerator.getAccessToken(user));
    }

    @Test
    @DisplayName("Login with valid credentials")
    public void loginWithValidCredentials() {
        Response response = authStep.sentPostToLoginPath(userGenerator.getLoginCredentials(user));
        authStep.checkStatusCode(response, SC_OK);
        authStep.checkSuccess(response, true);
        authStep.checkAccessToken(response);
        authStep.checkRefreshToken(response);
        authStep.checkUser(response, user.getEmail(), user.getName());
    }
}
