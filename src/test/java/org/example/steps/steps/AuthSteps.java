package org.example.steps.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.helpers.AbstractHelpers;
import org.example.pojo.auth.AuthRequest;
import org.example.pojo.auth.AuthResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;

public class AuthSteps extends AbstractHelpers {
    private static final String ACCESS_TOKEN_PATTER = "^Bearer\\s.+$";

    @Step("Send POST request")
    public static Response sendPostRequest(AuthRequest request, String basePath) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .body(request)
                .post(basePath);
    }

    @Step("Send PATCH request")
    public static Response sendPatchRequest(AuthRequest request, String basePath, String token) {
        if (token != null) {
            return given()
                    .spec(REQUEST_SPECIFICATION)
                    .auth()
                    .oauth2(token)
                    .body(request)
                    .patch(basePath);
        } else {
            return given()
                    .spec(REQUEST_SPECIFICATION)
                    .body(request)
                    .patch(basePath);
        }
    }

    @Step("Check status code")
    public static void checkStatusCode(Response response, int i) {
        response.then().statusCode(i);
    }

    @Step("Check body success boolean")
    public static void checkSuccess(Response response, boolean expected) {
        assertThat(response.as(AuthResponse.class).isSuccess(), equalTo(expected));
    }

    @Step("Check body message")
    public static void checkMessage(Response response, String expected) {
        assertThat(response.as(AuthResponse.class).getMessage(), equalTo(expected));
    }

    @Step("Check that body returns right user")
    public static void checkUser(Response response, String email, String name) {
        assertThat(response.as(AuthResponse.class).getUser().getEmail(), equalTo(email));
        assertThat(response.as(AuthResponse.class).getUser().getName(), equalTo(name));
    }

    @Step("Check that body returns access token")
    public static void checkAccessToken(Response response) {
        assertThat(response.as(AuthResponse.class).getAccessToken(), matchesPattern(ACCESS_TOKEN_PATTER));
    }

    @Step("Check that body returns refreshToken")
    public static void checkRefreshToken(Response response) {
        assertThat(response.as(AuthResponse.class).getRefreshToken(), notNullValue());
    }
}
