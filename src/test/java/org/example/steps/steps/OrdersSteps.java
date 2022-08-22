package org.example.steps.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.pojo.orders.OrderRequest;
import org.example.pojo.orders.OrdersResponse;

import static io.restassured.RestAssured.given;
import static org.example.helpers.UrlAndSpec.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.apache.http.HttpStatus.*;

public class OrdersSteps {

    @Step("Send POST request to: " + "\"" + ORDERS_PATH + "\"")
    public Response sendPostRequest(OrderRequest request, String token) {
        if (token != null) {
            return given()
                    .spec(REQUEST_SPECIFICATION)
                    .auth()
                    .oauth2(token)
                    .body(request)
                    .post(ORDERS_PATH);
        } else {
            return given()
                    .spec(REQUEST_SPECIFICATION)
                    .body(request)
                    .post(ORDERS_PATH);
        }

    }

    @Step("Sent GET request tp: " + "\"" + ORDERS_PATH + "\"")
    public Response sendGetRequest(String token) {
        if (token != null) {
            return given()
                    .baseUri(BASE_URI)
                    .auth()
                    .oauth2(token)
                    .get(ORDERS_PATH);
        } else {
            return given()
                    .baseUri(BASE_URI)
                    .get(ORDERS_PATH);
        }
    }

    @Step("Check status code")
    public void checkStatusCode(Response response, int i) {
        response.then().statusCode(i);
    }

    @Step("Check that body returns name")
    public void checkNameNotNull(Response response) {
        assertThat(response.as(OrdersResponse.class).getName(), notNullValue());
    }

    @Step("Check that body returns order number")
    public void checkOrderNumber(Response response, String ingredient) {
        assertThat(response.as(OrdersResponse.class).getOrder().getNumber(), notNullValue());
    }

    @Step("Check body success boolean")
    public void checkSuccess(Response response, boolean expected) {
        assertThat(response.as(OrdersResponse.class).isSuccess(), equalTo(expected));
    }

    @Step("Check body message")
    public void checkMessage(Response response, String expected) {
        assertThat(response.as(OrdersResponse.class).getMessage(), equalTo(expected));
    }

    @Step("Check that body returns order list")
    public void checkOrders(Response response, int expectedOrders) {
        assertThat(response.as(OrdersResponse.class).getOrders().size(), equalTo(expectedOrders));
    }

    @Step("Check that body returns total")
    public void checkTotalNotNull(Response response) {
        assertThat(response.as(OrdersResponse.class).getTotal(), notNullValue());
    }

    @Step("Check that body returns total today")
    public void checkTotalTodayNotNull(Response response) {
        assertThat(response.as(OrdersResponse.class).getTotalToday(), notNullValue());
    }

    @Step("Check that body return right owner")
    public void checkOwner(Response response, String expectedEmail, String expectedName) {
        assertThat(response.as(OrdersResponse.class).getOrder().getOwner().getEmail(), equalTo(expectedEmail));
        assertThat(response.as(OrdersResponse.class).getOrder().getOwner().getName(), equalTo(expectedName));
    }

    @Step("Create order")
    public int createOrder(OrderRequest request, String token, int numberOfOrders) {
        given()
                .spec(REQUEST_SPECIFICATION)
                .auth()
                .oauth2(token)
                .body(request)
                .post(ORDERS_PATH)
                .then()
                .statusCode(SC_OK);
        return ++numberOfOrders;
    }
}
