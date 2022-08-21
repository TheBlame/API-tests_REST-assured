package org.example.helpers;

import com.github.javafaker.Faker;
import org.example.pojo.auth.AuthRequest;

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
}
