package org.example.helpers;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public abstract class UrlAndSpec {
    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    public static final String LOGIN_PATH = "/api/auth/login";
    public static final String REGISTER_PATH = "/api/auth/register";
    public static final String USER_PATH = "/api/auth/user";
    public static final String ORDERS_PATH = "/api/orders";
    public static final String INGREDIENTS_PATH = "/api/ingredients";
    public static final RequestSpecification REQUEST_SPECIFICATION =
            new RequestSpecBuilder()
                    .setContentType(ContentType.JSON)
                    .setBaseUri(BASE_URI)
                    .build();
}
