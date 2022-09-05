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
    private static AuthRequest testUser;
    private static AuthRequest userToCheckUsedData;
    private static AuthRequest request;
    private static String testUserToken;

    @BeforeClass
    public static void setUp() {
        testUser = userGenerator.generateUser();
        userToCheckUsedData = userGenerator.generateUser();
        authStep.registerUser(testUser);
        authStep.registerUser(userToCheckUsedData);
        testUserToken = userGenerator.getAccessToken(testUser);
    }

    @AfterClass
    public static void clean() {
        authStep.deleteUser(testUserToken);
        authStep.deleteUser(userGenerator.getAccessToken(userToCheckUsedData));
    }

    @Test
    @DisplayName("Change unauthorized user data")
    public void changeUnauthorizedUserData() {
        request = userGenerator.changeUserCredentials(faker.internet().emailAddress(), faker.name().username(), faker.bothify("#?#?#?"));
        Response response = authStep.sendPatchToUserPath(request, null);
        authStep.checkStatusCode(response, SC_UNAUTHORIZED);
        authStep.checkSuccess(response, false);
        authStep.checkMessage(response, UNAUTHORIZED_MESSAGE);
    }

    @Test
    @DisplayName("Change user email to an email that already in use")
    public void changeEmailToAlreadyUsed() {
        request = userGenerator.changeUserCredentials(userToCheckUsedData.getEmail(), null, null);
        Response response = authStep.sendPatchToUserPath(request, testUserToken);
        authStep.checkStatusCode(response, SC_FORBIDDEN);
        authStep.checkSuccess(response, false);
        authStep.checkMessage(response, USED_EMAIL_MESSAGE);
    }
}
