package org.example.steps.auth.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.pojo.auth.AuthRequest;
import org.example.steps.AbstractTest;
import org.example.steps.steps.AuthSteps;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.*;

@RunWith(Parameterized.class)
public class ChangeUserDataParameterizedTest extends AbstractTest {
    private static final String CHANGED_NAME = FAKER.name().username();
    private static final String CHANGED_EMAIL = FAKER.internet().emailAddress();
    private static final String CHANGED_PASSWORD = FAKER.bothify("#?#?#?");
    private static final AuthRequest TEST_USER = USER_GENERATOR.generateUser();
    private final AuthSteps step = new AuthSteps();
    private static String testUserToken;

    private final AuthRequest request;
    private final String expectedEmail;
    private final String expectedName;

    public ChangeUserDataParameterizedTest(String message, AuthRequest request, String expectedEmail, String expectedName) {
        this.request = request;
        this.expectedEmail = expectedEmail;
        this.expectedName = expectedName;
    }

    @Parameterized.Parameters(name = "Test data: {0}")
    public static Object[][] getTestData() {
        return new Object[][] {
                {"Change user email", UTILS.changeUserCredentials(CHANGED_EMAIL, null, null),
                        CHANGED_EMAIL, TEST_USER.getName()},
                {"Change user name", UTILS.changeUserCredentials(null, CHANGED_NAME, null),
                        CHANGED_EMAIL, CHANGED_NAME},
                {"Change user password", UTILS.changeUserCredentials(null, null, CHANGED_PASSWORD),
                        CHANGED_EMAIL, CHANGED_NAME}
        };
    }

    @BeforeClass
    public static void setUp() {
        UTILS.registerUser(TEST_USER);
        testUserToken = UTILS.getAccessToken(TEST_USER);
    }

    @AfterClass
    public static void clean() {
        UTILS.deleteUser(testUserToken);
    }

    @Test
    @DisplayName("Change authorized user data")
    public void changeUserData() {
        Response response = step.sendPatchToUserPath(request, testUserToken);
        step.checkStatusCode(response, SC_OK);
        step.checkSuccess(response, true);
        step.checkUser(response, expectedEmail, expectedName);
    }
}
