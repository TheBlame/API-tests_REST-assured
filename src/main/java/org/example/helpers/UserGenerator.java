package org.example.helpers;

import com.github.javafaker.Faker;
import org.example.pojo.auth.AuthRequest;
import org.example.pojo.auth.AuthResponse;

import static io.restassured.RestAssured.given;
import static org.example.helpers.UrlAndSpec.LOGIN_PATH;
import static org.example.helpers.UrlAndSpec.REQUEST_SPECIFICATION;

public class UserGenerator {
    private final Faker faker = new Faker();

    public AuthRequest generateUser() {
        return AuthRequest.builder()
                .email(faker.internet().emailAddress())
                .name(faker.name().username())
                .password(faker.bothify("#?#?#?"))
                .build();
    }

    public AuthRequest generateUserWithoutEmail() {
        return AuthRequest.builder()
                .email(null)
                .name(faker.name().username())
                .password(faker.bothify("#?#?#?"))
                .build();
    }

    public AuthRequest generateUserWithoutPassword() {
        return AuthRequest.builder()
                .email(faker.internet().emailAddress())
                .name(null)
                .password(faker.bothify("#?#?#?"))
                .build();
    }

    public AuthRequest generateUserWithoutName() {
        return AuthRequest.builder()
                .email(faker.internet().emailAddress())
                .name(faker.name().username())
                .password(null)
                .build();
    }

    public AuthRequest generateCredentialsWithWrongEmail(AuthRequest request) {
        String email = request.getEmail();
        boolean equal = true;
        while (equal) {
            if (email.equals(request.getEmail())) {
                email = faker.internet().emailAddress();
            } else {
                equal = false;
            }
        }
        return AuthRequest.builder()
                .email(email)
                .name(null)
                .password(request.getPassword())
                .build();
    }

    public AuthRequest generateCredentialsWithWrongPassword(AuthRequest request) {
        String password = request.getPassword();
        boolean equal = true;
        while (equal) {
            if (password.equals(request.getPassword())) {
                password = faker.bothify("#?#?#?");
            } else {
                equal = false;
            }
        }
        return AuthRequest.builder()
                .email(request.getEmail())
                .name(null)
                .password(password)
                .build();
    }

    public AuthRequest changeUserCredentials(String newEmail, String newName, String newPassword) {
        return AuthRequest.builder()
                .email(newEmail)
                .name(newName)
                .password(newPassword)
                .build();
    }

    public AuthRequest getLoginCredentials(AuthRequest request) {
        return AuthRequest.builder()
                .email(request.getEmail())
                .name(null)
                .password(request.getPassword())
                .build();
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
}
