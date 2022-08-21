package org.example.pojo.auth;

import lombok.Getter;
import lombok.Setter;
import org.example.pojo.auth.nested_classes.User;

@Getter
@Setter
public class AuthResponse {
    private boolean success;
    private String message;
    private User user;
    private String accessToken;
    private String refreshToken;
}
