package org.example.steps.orders;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.pojo.auth.AuthRequest;
import org.example.pojo.orders.OrderRequest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.example.helpers.OrderGenerator.*;
import static org.example.helpers.UserGenerator.*;
import static org.example.helpers.Utils.*;
import static org.example.steps.steps.OrdersSteps.*;

public class GetUserOrdersTests {
    private static final String BASE_PATH = "/api/orders";
    private static final String UNAUTHORIZED_USER_MESSAGE = "You should be authorised";
    private static final AuthRequest TEST_USER = generateUser();
    private static String testUserToken;
    private static int numberOfOrders = 0;

    @BeforeClass
    public static void setUp() {
        registerUser(TEST_USER);
        testUserToken = getAccessToken(TEST_USER);
        OrderRequest order = generateOrderWithRandomIngredient();
        numberOfOrders = createOrder(order, testUserToken, numberOfOrders);
    }

    @AfterClass
    public static void clean() {
        deleteUser(testUserToken);
    }

    @Test
    @DisplayName("Get authorised user orders")
    public void getAuthorizedUserOrders() {
        Response response = sendGetRequest(BASE_PATH, testUserToken);
        checkStatusCode(response, SC_OK);
        checkSuccess(response, true);
        checkOrders(response, numberOfOrders);
        checkTotalNotNull(response);
        checkTotalTodayNotNull(response);
    }

    @Test
    @DisplayName("Get unauthorized user orders")
    public void getUnauthorizedUserOrders() {
        Response response = sendGetRequest(BASE_PATH, null);
        checkStatusCode(response, SC_UNAUTHORIZED);
        checkSuccess(response, false);
        checkMessage(response, UNAUTHORIZED_USER_MESSAGE);
    }
}
