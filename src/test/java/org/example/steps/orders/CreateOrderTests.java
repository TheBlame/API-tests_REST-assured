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

public class CreateOrderTests extends AbstractTest {
    private static final String NO_INGREDIENTS_MESSAGE = "Ingredient ids must be provided";
    private static final AuthRequest TEST_USER = USER_GENERATOR.generateUser();
    private final OrderRequest validRequest = ORDER_GENERATOR.generateOrderWithRandomIngredient();
    private static String testUserToken;
    private OrderRequest invalidRequest;

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
    @DisplayName("Create valid order without authorization")
    public void createOrderWithoutAuthorization() {
        Response response = ORDERS_STEP.sendPostRequest(validRequest, null);
        ORDERS_STEP.checkStatusCode(response, SC_OK);
        ORDERS_STEP.checkSuccess(response, true);
        ORDERS_STEP.checkNameNotNull(response);
        ORDERS_STEP.checkOrderNumber(response, validRequest.getIngredients().get(0));
    }

    @Test
    @DisplayName("Create valid order with authorization")
    public void createOrderWithAuthorization() {
        Response response = ORDERS_STEP.sendPostRequest(validRequest, testUserToken);
        ORDERS_STEP.checkStatusCode(response, SC_OK);
        ORDERS_STEP.checkSuccess(response, true);
        ORDERS_STEP.checkNameNotNull(response);
        ORDERS_STEP.checkOrderNumber(response, validRequest.getIngredients().get(0));
        ORDERS_STEP.checkOwner(response, TEST_USER.getEmail(), TEST_USER.getName());
    }

    @Test
    @DisplayName("Create order without ingredients")
    public void createOrderWithoutIngredients() {
        invalidRequest = ORDER_GENERATOR.generateOrderWithoutIngredients();
        Response response = ORDERS_STEP.sendPostRequest(invalidRequest, null);
        ORDERS_STEP.checkStatusCode(response, SC_BAD_REQUEST);
        ORDERS_STEP.checkSuccess(response, false);
        ORDERS_STEP.checkMessage(response, NO_INGREDIENTS_MESSAGE);
    }

    @Test
    @DisplayName("Create order with invalid ingredient hash")
    public void createOrderWithInvalidIngredient() {
        invalidRequest = ORDER_GENERATOR.generateOrderWithInvalidIngredientHash();
        Response response = ORDERS_STEP.sendPostRequest(invalidRequest, null);
        ORDERS_STEP.checkStatusCode(response, SC_INTERNAL_SERVER_ERROR);
    }
}
