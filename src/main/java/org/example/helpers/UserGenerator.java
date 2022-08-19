package org.example.helpers;

import com.github.javafaker.Faker;
import org.example.pojo.auth.AuthRequest;

public class UserGenerator {
    private static final Faker FAKER = new Faker();

    public static AuthRequest generateUser() {
        return new AuthRequest(FAKER.internet().emailAddress(), FAKER.name().username(), FAKER.bothify("#?#?#?"));
    }

    public static AuthRequest generateUserWithoutEmail() {
        return new AuthRequest(null, FAKER.name().username(), FAKER.bothify("#?#?#?"));
    }

    public static AuthRequest generateUserWithoutPassword() {
        return new AuthRequest(FAKER.internet().emailAddress(), null, FAKER.bothify("#?#?#?"));
    }

    public static AuthRequest generateUserWithoutName() {
        return new AuthRequest(FAKER.internet().emailAddress(), FAKER.name().username(), null);
    }

    public static AuthRequest generateCredentialsWithWrongEmail(AuthRequest request) {
        String email = request.getEmail();
        boolean equal = true;
        while (equal) {
            if (email.equals(request.getEmail())) {
                email = FAKER.internet().emailAddress();
            } else {
                equal = false;
            }
        }
        return new AuthRequest(email, null, request.getPassword());
    }

    public static AuthRequest generateCredentialsWithWrongPassword(AuthRequest request) {
        String password = request.getPassword();
        boolean equal = true;
        while (equal) {
            if (password.equals(request.getPassword())) {
                password = FAKER.bothify("#?#?#?");
            } else {
                equal = false;
            }
        }
        return new AuthRequest(request.getEmail(), null, password);
    }
}
