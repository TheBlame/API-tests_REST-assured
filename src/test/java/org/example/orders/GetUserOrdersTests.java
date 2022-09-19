package org.example.orders;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.pojo.auth.AuthRequest;
import org.example.pojo.orders.OrderRequest;
import org.example.AbstractTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class GetUserOrdersTests extends AbstractTest {
    private static final String UNAUTHORIZED_USER_MESSAGE = "You should be authorised";
    private static final AuthRequest testUser = userGenerator.generateUser();
    private static String testUserToken;
    private static int numberOfOrders = 0;

    @BeforeClass
    public static void setUp() {
        authStep.registerUser(testUser);
        testUserToken = userGenerator.getAccessToken(testUser);
        OrderRequest order = orderGenerator.generateOrderWithRandomIngredient();
        numberOfOrders = ordersStep.createOrder(order, testUserToken, numberOfOrders);
    }

    @AfterClass
    public static void clean() {
        authStep.deleteUser(testUserToken);
    }

    @Test
    @DisplayName("Get authorised user orders")
    public void getAuthorizedUserOrders() {
        Response response = ordersStep.sendGetRequest(testUserToken);
        ordersStep.checkStatusCode(response, SC_OK);
        ordersStep.checkSuccess(response, true);
        ordersStep.checkOrders(response, numberOfOrders);
        ordersStep.checkTotalNotNull(response);
        ordersStep.checkTotalTodayNotNull(response);
    }

    @Test
    @DisplayName("Get unauthorized user orders")
    public void getUnauthorizedUserOrders() {
        Response response = ordersStep.sendGetRequest(null);
        ordersStep.checkStatusCode(response, SC_UNAUTHORIZED);
        ordersStep.checkSuccess(response, false);
        ordersStep.checkMessage(response, UNAUTHORIZED_USER_MESSAGE);
    }
}
