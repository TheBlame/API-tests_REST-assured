package org.example.steps.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.pojo.auth.AuthRequest;
import org.example.pojo.auth.AuthResponse;

import static io.restassured.RestAssured.given;
import static org.example.helpers.UrlAndSpec.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.apache.http.HttpStatus.*;

public class AuthSteps {
    private static final String ACCESS_TOKEN_PATTER = "^Bearer\\s.+$";

    @Step("Send POST request to: " + "\"" + LOGIN_PATH + "\"")
    public Response sentPostToLoginPath(AuthRequest request) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .body(request)
                .post(LOGIN_PATH);
    }

    @Step("Send POST request to: " + "\"" + REGISTER_PATH + "\"")
    public Response sentPostToRegisterPath(AuthRequest request) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .body(request)
                .post(REGISTER_PATH);
    }

    @Step("Send PATCH request to: " + "\"" + USER_PATH + "\"")
    public Response sendPatchToUserPath(AuthRequest request, String token) {
        if (token != null) {
            return given()
                    .spec(REQUEST_SPECIFICATION)
                    .auth()
                    .oauth2(token)
                    .body(request)
                    .patch(USER_PATH);
        } else {
            return given()
                    .spec(REQUEST_SPECIFICATION)
                    .body(request)
                    .patch(USER_PATH);
        }
    }

    @Step("Check status code")
    public void checkStatusCode(Response response, int i) {
        response.then().statusCode(i);
    }

    @Step("Check body success boolean")
    public void checkSuccess(Response response, boolean expected) {
        assertThat(response.as(AuthResponse.class).isSuccess(), equalTo(expected));
    }

    @Step("Check body message")
    public void checkMessage(Response response, String expected) {
        assertThat(response.as(AuthResponse.class).getMessage(), equalTo(expected));
    }

    @Step("Check that body returns right user")
    public void checkUser(Response response, String email, String name) {
        assertThat(response.as(AuthResponse.class).getUser().getEmail(), equalTo(email));
        assertThat(response.as(AuthResponse.class).getUser().getName(), equalTo(name));
    }

    @Step("Check that body returns access token")
    public void checkAccessToken(Response response) {
        assertThat(response.as(AuthResponse.class).getAccessToken(), matchesPattern(ACCESS_TOKEN_PATTER));
    }

    @Step("Check that body returns refreshToken")
    public void checkRefreshToken(Response response) {
        assertThat(response.as(AuthResponse.class).getRefreshToken(), notNullValue());
    }

    @Step("Delete user")
    public void deleteUser(String token) {
        if (token != null) {
            given()
                    .spec(REQUEST_SPECIFICATION)
                    .auth()
                    .oauth2(token)
                    .delete(USER_PATH)
                    .then()
                    .statusCode(SC_ACCEPTED);
        }
    }

    @Step("Register user")
    public void registerUser(AuthRequest request) {
        given()
                .spec(REQUEST_SPECIFICATION)
                .body(request)
                .when()
                .post(REGISTER_PATH)
                .then()
                .statusCode(SC_OK);
    }
}
