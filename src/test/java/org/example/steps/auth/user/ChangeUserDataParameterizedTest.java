package org.example.steps.auth.user;

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
public class ChangeUserDataParameterizedTest extends AbstractTest {
    private static final String nameForChange = faker.name().username();
    private static final String emailForChange = faker.internet().emailAddress();
    private static final String passwordForChange = faker.bothify("#?#?#?");
    private static final AuthRequest testUser = userGenerator.generateUser();
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
                {"Change user email", userGenerator.changeUserCredentials(emailForChange, null, null),
                        emailForChange, testUser.getName()},
                {"Change user name", userGenerator.changeUserCredentials(null, nameForChange, null),
                        emailForChange, nameForChange},
                {"Change user password", userGenerator.changeUserCredentials(null, null, passwordForChange),
                        emailForChange, nameForChange}
        };
    }

    @BeforeClass
    public static void setUp() {
        authStep.registerUser(testUser);
        testUserToken = userGenerator.getAccessToken(testUser);
    }

    @AfterClass
    public static void clean() {
        authStep.deleteUser(testUserToken);
    }

    @Test
    @DisplayName("Change authorized user data")
    public void changeUserData() {
        Response response = authStep.sendPatchToUserPath(request, testUserToken);
        authStep.checkStatusCode(response, SC_OK);
        authStep.checkSuccess(response, true);
        authStep.checkUser(response, expectedEmail, expectedName);
    }
}
