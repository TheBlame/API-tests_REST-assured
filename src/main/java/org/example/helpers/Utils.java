package org.example.helpers;

import org.example.pojo.auth.AuthRequest;
import org.example.pojo.auth.AuthResponse;
import org.example.pojo.orders.OrderRequest;

import static io.restassured.RestAssured.given;

public class Utils extends AbstractHelpers {
    public static void deleteUser(String token) {
        given()
                .baseUri(BASE_URI)
                .auth()
                .oauth2(token)
                .delete("/api/auth/user");
    }

    public static String getAccessToken(AuthRequest request) {
        String token = given()
                .spec(REQUEST_SPECIFICATION)
                .body(getLoginCredentials(request))
                .post("/api/auth/login")
                .as(AuthResponse.class)
                .getAccessToken();
        return token.substring(7);
    }

    public static AuthRequest getLoginCredentials(AuthRequest request) {
        return new AuthRequest(request.getEmail(), null, request.getPassword());
    }

    public static void registerUser(AuthRequest request) {
        given()
                .spec(REQUEST_SPECIFICATION)
                .body(request)
                .when()
                .post("/api/auth/register");
    }

    public static AuthRequest changeUserCredentials(String newEmail, String newName, String newPassword) {
        return new AuthRequest(newEmail, newName, newPassword);
    }

    public static int createOrder(OrderRequest request, String token, int numberOfOrders) {
        given()
                .spec(REQUEST_SPECIFICATION)
                .auth()
                .oauth2(token)
                .body(request)
                .post("/api/orders");
        return ++numberOfOrders;
    }
}
