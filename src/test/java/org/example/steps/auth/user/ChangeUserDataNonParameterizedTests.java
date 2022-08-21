package org.example.steps.auth.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.pojo.auth.AuthRequest;
import org.example.steps.AbstractTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class ChangeUserDataNonParameterizedTests extends AbstractTest {
    private static final String UNAUTHORIZED_MESSAGE = "You should be authorised";
    private static final String USED_EMAIL_MESSAGE = "User with such email already exists";
    private static final AuthRequest TEST_USER = USER_GENERATOR.generateUser();
    private static final AuthRequest USER_TO_CHECK_USED_DATA = USER_GENERATOR.generateUser();
    private static AuthRequest request;
    private static String testUserToken;

    @BeforeClass
    public static void setUp() {
        UTILS.registerUser(TEST_USER);
        testUserToken = UTILS.getAccessToken(TEST_USER);
        UTILS.registerUser(USER_TO_CHECK_USED_DATA);
    }

    @AfterClass
    public static void clean() {
        UTILS.deleteUser(testUserToken);
        UTILS.deleteUser(UTILS.getAccessToken(USER_TO_CHECK_USED_DATA));
    }

    @Test
    @DisplayName("Change unauthorized user data")
    public void changeUnauthorizedUserData() {
        request = UTILS.changeUserCredentials(FAKER.internet().emailAddress(), FAKER.name().username(), FAKER.bothify("#?#?#?"));
        Response response = AUTH_STEP.sendPatchToUserPath(request, null);
        AUTH_STEP.checkStatusCode(response, SC_UNAUTHORIZED);
        AUTH_STEP.checkSuccess(response, false);
        AUTH_STEP.checkMessage(response, UNAUTHORIZED_MESSAGE);
    }

    @Test
    @DisplayName("Change user email to an email that already in use")
    public void changeEmailToAlreadyUsed() {
        request = UTILS.changeUserCredentials(USER_TO_CHECK_USED_DATA.getEmail(), null, null);
        Response response = AUTH_STEP.sendPatchToUserPath(request, testUserToken);
        AUTH_STEP.checkStatusCode(response, SC_FORBIDDEN);
        AUTH_STEP.checkSuccess(response, false);
        AUTH_STEP.checkMessage(response, USED_EMAIL_MESSAGE);
    }
}
