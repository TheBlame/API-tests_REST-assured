package org.example.steps;

import com.github.javafaker.Faker;
import org.example.helpers.OrderGenerator;
import org.example.helpers.UserGenerator;
import org.example.helpers.Utils;
import org.example.steps.steps.AuthSteps;
import org.example.steps.steps.OrdersSteps;

public abstract class AbstractTest {
    public static final UserGenerator USER_GENERATOR = new UserGenerator();
    public static final OrderGenerator ORDER_GENERATOR = new OrderGenerator();
    public static final Utils UTILS = new Utils();
    public static final AuthSteps AUTH_STEP = new AuthSteps();
    public static final OrdersSteps ORDERS_STEP = new OrdersSteps();
    public static final Faker FAKER = new Faker();
}
