package org.example.steps;

import com.github.javafaker.Faker;
import org.example.helpers.OrderGenerator;
import org.example.helpers.UserGenerator;
import org.example.steps.steps.AuthSteps;
import org.example.steps.steps.OrdersSteps;

public abstract class AbstractTest {
    public static UserGenerator userGenerator = new UserGenerator();
    public static OrderGenerator orderGenerator = new OrderGenerator();
    public static AuthSteps authStep = new AuthSteps();
    public static OrdersSteps ordersStep = new OrdersSteps();
    public static Faker faker = new Faker();
}
