package org.example.helpers;

import org.example.pojo.auth.AuthRequest;
import org.example.pojo.auth.AuthResponse;
import org.example.pojo.orders.OrderRequest;

import static io.restassured.RestAssured.given;
import static org.example.helpers.UrlAndSpec.*;

public class Utils {
    public void deleteUser(String token) {
        if (token != null) {
            given()
                    .spec(REQUEST_SPECIFICATION)
                    .auth()
                    .oauth2(token)
                    .delete(USER_PATH);
        }
    }

    public String getAccessToken(AuthRequest request) {
        String token = given()
                .spec(REQUEST_SPECIFICATION)
                .body(getLoginCredentials(request))
                .post(LOGIN_PATH)
                .as(AuthResponse.class)
                .getAccessToken();
        if (token != null) {
            return token.substring(7);
        } else {
            return null;
        }
    }

    public AuthRequest getLoginCredentials(AuthRequest request) {
        return AuthRequest.builder()
                .email(request.getEmail())
                .name(null)
                .password(request.getPassword())
                .build();
    }

    public void registerUser(AuthRequest request) {
        given()
                .spec(REQUEST_SPECIFICATION)
                .body(request)
                .when()
                .post(REGISTER_PATH);
    }

    public AuthRequest changeUserCredentials(String newEmail, String newName, String newPassword) {
        return AuthRequest.builder()
                .email(newEmail)
                .name(newName)
                .password(newPassword)
                .build();
    }

    public int createOrder(OrderRequest request, String token, int numberOfOrders) {
        given()
                .spec(REQUEST_SPECIFICATION)
                .auth()
                .oauth2(token)
                .body(request)
                .post(ORDERS_PATH);
        return ++numberOfOrders;
    }
}
