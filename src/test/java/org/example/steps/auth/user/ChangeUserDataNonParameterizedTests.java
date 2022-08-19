package org.example.steps.auth.user;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.pojo.auth.AuthRequest;
import org.junit.*;

import static org.apache.http.HttpStatus.*;
import static org.example.helpers.UserGenerator.*;
import static org.example.helpers.Utils.*;
import static org.example.steps.steps.AuthSteps.*;

public class ChangeUserDataNonParameterizedTests {
    private static final String BASE_PATH = "/api/auth/user";
    private static final String UNAUTHORIZED_MESSAGE = "You should be authorised";
    private static final String USED_EMAIL_MESSAGE = "User with such email already exists";
    private static final AuthRequest TEST_USER = generateUser();
    private static final AuthRequest USER_TO_CHECK_USED_DATA = generateUser();
    private static final Faker FAKER = new Faker();

    private static AuthRequest request;
    private static String testUserToken;

    @BeforeClass
    public static void setUp() {
        registerUser(TEST_USER);
        testUserToken = getAccessToken(TEST_USER);
        registerUser(USER_TO_CHECK_USED_DATA);
    }

    @AfterClass
    public static void clean() {
        deleteUser(testUserToken);
        deleteUser(getAccessToken(USER_TO_CHECK_USED_DATA));
    }

    @Test
    @DisplayName("Change unauthorized user data")
    public void changeUnauthorizedUserData() {
        request = changeUserCredentials(FAKER.internet().emailAddress(), FAKER.name().username(), FAKER.bothify("#?#?#?"));
        Response response = sendPatchRequest(request, BASE_PATH, null);
        checkStatusCode(response, SC_UNAUTHORIZED);
        checkSuccess(response, false);
        checkMessage(response, UNAUTHORIZED_MESSAGE);
    }

    @Test
    @DisplayName("Change user email to an email that already in use")
    public void changeEmailToAlreadyUsed() {
        request = changeUserCredentials(USER_TO_CHECK_USED_DATA.getEmail(), null, null);
        Response response = sendPatchRequest(request, BASE_PATH, testUserToken);
        checkStatusCode(response, SC_FORBIDDEN);
        checkSuccess(response, false);
        checkMessage(response, USED_EMAIL_MESSAGE);
    }
}
