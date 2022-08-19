package org.example.steps.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.helpers.AbstractHelpers;
import org.example.pojo.orders.OrderRequest;
import org.example.pojo.orders.OrdersResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class OrdersSteps extends AbstractHelpers {

    @Step("Send POST request")
    public static Response sendPostRequest(OrderRequest request, String basePath, String token) {
        if (token != null) {
            return given()
                    .spec(REQUEST_SPECIFICATION)
                    .auth()
                    .oauth2(token)
                    .body(request)
                    .post(basePath);
        } else {
            return given()
                    .spec(REQUEST_SPECIFICATION)
                    .body(request)
                    .post(basePath);
        }

    }

    @Step("Sent GET request")
    public static Response sendGetRequest(String basePath, String token) {
        if (token != null) {
            return given()
                    .baseUri(BASE_URI)
                    .auth()
                    .oauth2(token)
                    .get(basePath);
        } else {
            return given()
                    .baseUri(BASE_URI)
                    .get(basePath);
        }
    }

    @Step("Check status code")
    public static void checkStatusCode(Response response, int i) {
        response.then().statusCode(i);
    }

    @Step("Check that body returns name")
    public static void checkNameNotNull(Response response) {
        assertThat(response.as(OrdersResponse.class).getName(), notNullValue());
    }

    @Step("Check that body returns order number")
    public static void checkOrderNumber(Response response, String ingredient) {
        assertThat(response.as(OrdersResponse.class).getOrder().getNumber(), notNullValue());
    }

    @Step("Check body success boolean")
    public static void checkSuccess(Response response, boolean expected) {
        assertThat(response.as(OrdersResponse.class).isSuccess(), equalTo(expected));
    }

    @Step("Check body message")
    public static void checkMessage(Response response, String expected) {
        assertThat(response.as(OrdersResponse.class).getMessage(), equalTo(expected));
    }

    @Step("Check that body returns order list")
    public static void checkOrders(Response response, int expectedOrders) {
        assertThat(response.as(OrdersResponse.class).getOrders().size(), equalTo(expectedOrders));
    }

    @Step("Check that body returns total")
    public static void checkTotalNotNull(Response response) {
        assertThat(response.as(OrdersResponse.class).getTotal(), notNullValue());
    }

    @Step("Check that body returns total today")
    public static void checkTotalTodayNotNull(Response response) {
        assertThat(response.as(OrdersResponse.class).getTotalToday(), notNullValue());
    }

    @Step("Check that body return right owner")
    public static void checkOwner(Response response, String expectedEmail, String expectedName) {
        assertThat(response.as(OrdersResponse.class).getOrder().getOwner().getEmail(), equalTo(expectedEmail));
        assertThat(response.as(OrdersResponse.class).getOrder().getOwner().getName(), equalTo(expectedName));
    }
}
