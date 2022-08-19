package org.example.steps.auth.login;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.pojo.auth.AuthRequest;
import org.junit.*;

import static org.apache.http.HttpStatus.*;
import static org.example.helpers.UserGenerator.*;
import static org.example.helpers.Utils.*;
import static org.example.steps.steps.AuthSteps.*;

public class LoginNonParameterizedTests {
    private static final String BASE_PATH = "/api/auth/login";
    private static final AuthRequest USER = generateUser();

    @BeforeClass
    public static void setUp() {
        registerUser(USER);
    }

    @AfterClass
    public static void clean() {
        deleteUser(getAccessToken(USER));
    }

    @Test
    @DisplayName("Login with valid credentials")
    public void loginWithValidCredentials() {
        Response response = sendPostRequest(getLoginCredentials(USER), BASE_PATH);
        checkStatusCode(response, SC_OK);
        checkSuccess(response, true);
        checkAccessToken(response);
        checkRefreshToken(response);
        checkUser(response, USER.getEmail(), USER.getName());
    }
}
