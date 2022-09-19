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

public class CreateOrderTests extends AbstractTest {
    private static final String NO_INGREDIENTS_MESSAGE = "Ingredient ids must be provided";
    private static AuthRequest testUser;
    private static OrderRequest validRequest;
    private static String testUserToken;
    private OrderRequest invalidRequest;

    @BeforeClass
    public static void setUp() {
        testUser = userGenerator.generateUser();
        validRequest = orderGenerator.generateOrderWithRandomIngredient();
        authStep.registerUser(testUser);
        testUserToken = userGenerator.getAccessToken(testUser);
    }

    @AfterClass
    public static void clean() {
        authStep.deleteUser(testUserToken);
    }

    @Test
    @DisplayName("Create valid order without authorization")
    public void createOrderWithoutAuthorization() {
        Response response = ordersStep.sendPostRequest(validRequest, null);
        ordersStep.checkStatusCode(response, SC_OK);
        ordersStep.checkSuccess(response, true);
        ordersStep.checkNameNotNull(response);
        ordersStep.checkOrderNumber(response, validRequest.getIngredients().get(0));
    }

    @Test
    @DisplayName("Create valid order with authorization")
    public void createOrderWithAuthorization() {
        Response response = ordersStep.sendPostRequest(validRequest, testUserToken);
        ordersStep.checkStatusCode(response, SC_OK);
        ordersStep.checkSuccess(response, true);
        ordersStep.checkNameNotNull(response);
        ordersStep.checkOrderNumber(response, validRequest.getIngredients().get(0));
        ordersStep.checkOwner(response, testUser.getEmail(), testUser.getName());
    }

    @Test
    @DisplayName("Create order without ingredients")
    public void createOrderWithoutIngredients() {
        invalidRequest = orderGenerator.generateOrderWithoutIngredients();
        Response response = ordersStep.sendPostRequest(invalidRequest, null);
        ordersStep.checkStatusCode(response, SC_BAD_REQUEST);
        ordersStep.checkSuccess(response, false);
        ordersStep.checkMessage(response, NO_INGREDIENTS_MESSAGE);
    }

    @Test
    @DisplayName("Create order with invalid ingredient hash")
    public void createOrderWithInvalidIngredient() {
        invalidRequest = orderGenerator.generateOrderWithInvalidIngredientHash();
        Response response = ordersStep.sendPostRequest(invalidRequest, null);
        ordersStep.checkStatusCode(response, SC_INTERNAL_SERVER_ERROR);
    }
}
