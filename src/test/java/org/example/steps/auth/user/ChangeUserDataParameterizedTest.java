package org.example.steps.auth.user;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.pojo.auth.AuthRequest;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.*;
import static org.example.helpers.UserGenerator.*;
import static org.example.helpers.Utils.*;
import static org.example.steps.steps.AuthSteps.*;

@RunWith(Parameterized.class)
public class ChangeUserDataParameterizedTest {
    private static final String BASE_PATH = "/api/auth/user";
    private static final Faker FAKER = new Faker();
    private static final String CHANGED_NAME = FAKER.name().username();
    private static final String CHANGED_EMAIL = FAKER.internet().emailAddress();
    private static final String CHANGED_PASSWORD = FAKER.bothify("#?#?#?");
    private static final AuthRequest TEST_USER = generateUser();
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
                {"Change user email", changeUserCredentials(CHANGED_EMAIL, null, null),
                        CHANGED_EMAIL, TEST_USER.getName()},
                {"Change user name", changeUserCredentials(null, CHANGED_NAME, null),
                        CHANGED_EMAIL, CHANGED_NAME},
                {"Change user password", changeUserCredentials(null, null, CHANGED_PASSWORD),
                        CHANGED_EMAIL, CHANGED_NAME}
        };
    }

    @BeforeClass
    public static void setUp() {
        registerUser(TEST_USER);
        testUserToken = getAccessToken(TEST_USER);
    }

    @AfterClass
    public static void clean() {
        deleteUser(testUserToken);
    }

    @Test
    @DisplayName("Change authorized user data")
    public void changeUserData() {
        Response response = sendPatchRequest(request, BASE_PATH, testUserToken);
        checkStatusCode(response, SC_OK);
        checkSuccess(response, true);
        checkUser(response, expectedEmail, expectedName);
    }
}
