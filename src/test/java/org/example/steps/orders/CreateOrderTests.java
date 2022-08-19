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

public class CreateOrderTests {
    private static final String BASE_PATH = "/api/orders";
    private static final String NO_INGREDIENTS_MESSAGE = "Ingredient ids must be provided";
    private static final AuthRequest TEST_USER = generateUser();
    private static final OrderRequest VALID_REQUEST = generateOrderWithRandomIngredient();
    private static String testUserToken;
    private OrderRequest invalidRequest;

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
    @DisplayName("Create valid order without authorization")
    public void createOrderWithoutAuthorization() {
        Response response = sendPostRequest(VALID_REQUEST, BASE_PATH, null);
        checkStatusCode(response, SC_OK);
        checkSuccess(response, true);
        checkNameNotNull(response);
        checkOrderNumber(response, VALID_REQUEST.getIngredients().get(0));
    }

    @Test
    @DisplayName("Create valid order with authorization")
    public void createOrderWithAuthorization() {
        Response response = sendPostRequest(VALID_REQUEST, BASE_PATH, testUserToken);
        checkStatusCode(response, SC_OK);
        checkSuccess(response, true);
        checkNameNotNull(response);
        checkOrderNumber(response, VALID_REQUEST.getIngredients().get(0));
        checkOwner(response, TEST_USER.getEmail(), TEST_USER.getName());
    }

    @Test
    @DisplayName("Create order without ingredients")
    public void createOrderWithoutIngredients() {
        invalidRequest = generateOrderWithoutIngredients();
        Response response = sendPostRequest(invalidRequest, BASE_PATH, null);
        checkStatusCode(response, SC_BAD_REQUEST);
        checkSuccess(response, false);
        checkMessage(response, NO_INGREDIENTS_MESSAGE);
    }

    @Test
    @DisplayName("Create order with invalid ingredient hash")
    public void createOrderWithInvalidIngredient() {
        invalidRequest = generateOrderWithInvalidIngredientHash();
        Response response = sendPostRequest(invalidRequest, BASE_PATH, null);
        checkStatusCode(response, SC_INTERNAL_SERVER_ERROR);
    }
}
