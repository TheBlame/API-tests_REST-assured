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
    private static final AuthRequest USER = USER_GENERATOR.generateUser();

    @BeforeClass
    public static void setUp() {
        UTILS.registerUser(USER);
    }

    @AfterClass
    public static void clean() {
        UTILS.deleteUser(UTILS.getAccessToken(USER));
    }

    @Test
    @DisplayName("Login with valid credentials")
    public void loginWithValidCredentials() {
        Response response = AUTH_STEP.sentPostToLoginPath(UTILS.getLoginCredentials(USER));
        AUTH_STEP.checkStatusCode(response, SC_OK);
        AUTH_STEP.checkSuccess(response, true);
        AUTH_STEP.checkAccessToken(response);
        AUTH_STEP.checkRefreshToken(response);
        AUTH_STEP.checkUser(response, USER.getEmail(), USER.getName());
    }
}
