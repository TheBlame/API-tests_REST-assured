package org.example.steps.orders;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.pojo.auth.AuthRequest;
import org.example.pojo.orders.OrderRequest;
import org.example.steps.AbstractTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class GetUserOrdersTests extends AbstractTest {
    private static final String UNAUTHORIZED_USER_MESSAGE = "You should be authorised";
    private static final AuthRequest TEST_USER = USER_GENERATOR.generateUser();
    private static String testUserToken;
    private static int numberOfOrders = 0;

    @BeforeClass
    public static void setUp() {
        UTILS.registerUser(TEST_USER);
        testUserToken = UTILS.getAccessToken(TEST_USER);
        OrderRequest order = ORDER_GENERATOR.generateOrderWithRandomIngredient();
        numberOfOrders = UTILS.createOrder(order, testUserToken, numberOfOrders);
    }

    @AfterClass
    public static void clean() {
        UTILS.deleteUser(testUserToken);
    }

    @Test
    @DisplayName("Get authorised user orders")
    public void getAuthorizedUserOrders() {
        Response response = ORDERS_STEP.sendGetRequest(testUserToken);
        ORDERS_STEP.checkStatusCode(response, SC_OK);
        ORDERS_STEP.checkSuccess(response, true);
        ORDERS_STEP.checkOrders(response, numberOfOrders);
        ORDERS_STEP.checkTotalNotNull(response);
        ORDERS_STEP.checkTotalTodayNotNull(response);
    }

    @Test
    @DisplayName("Get unauthorized user orders")
    public void getUnauthorizedUserOrders() {
        Response response = ORDERS_STEP.sendGetRequest(null);
        ORDERS_STEP.checkStatusCode(response, SC_UNAUTHORIZED);
        ORDERS_STEP.checkSuccess(response, false);
        ORDERS_STEP.checkMessage(response, UNAUTHORIZED_USER_MESSAGE);
    }
}
