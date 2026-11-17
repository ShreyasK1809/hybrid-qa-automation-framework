package com.framework.tests.api;

import com.framework.endpoints.UserApi;
import com.framework.models.UserPayload;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;

/** Pure API tests: no browser is started, so they run in parallel with the UI tests at no extra cost. */
public class UserApiTest {

    private final UserApi userApi = new UserApi();

    @Test(groups = {"smoke", "api"}, description = "GET single user: status, headers, body, JSON schema, response time")
    public void getSingleUser_returnsValidContract() {
        userApi.getUser(2).then()
                .statusCode(200)
                .header("Content-Type", containsString("application/json"))
                .time(lessThan(5000L))
                .body("data.id", equalTo(2))
                .body("data.email", matchesPattern(".+@.+\\..+"))
                .body("data.first_name", notNullValue())
                .body(matchesJsonSchemaInClasspath("schemas/single-user-schema.json"));
    }

    @Test(groups = {"regression", "api"}, description = "POST create user echoes payload and returns id + createdAt")
    public void createUser_returns201WithGeneratedId() {
        UserPayload payload = new UserPayload("Asha Rao", "QA Automation Engineer");

        Response response = userApi.createUser(payload);

        response.then()
                .statusCode(201)
                .header("Content-Type", containsString("application/json"))
                .body("name", equalTo(payload.name()))
                .body("job", equalTo(payload.job()))
                .body("id", notNullValue())
                .body("createdAt", notNullValue());

        String newId = response.jsonPath().getString("id");
        Assert.assertFalse(newId.isBlank(), "Generated id should not be blank");
    }

    @Test(groups = {"regression", "api"}, description = "Negative: login without password returns 400 + error message")
    public void login_withoutPassword_returns400() {
        userApi.login(Map.of("email", "peter@klaven")).then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test(groups = {"regression", "api"}, description = "Negative: unknown user returns 404 and empty JSON body")
    public void getUnknownUser_returns404() {
        Response response = userApi.getUser(9999);
        Assert.assertEquals(response.statusCode(), 404, "Expected 404 for non-existing user");
        Assert.assertEquals(response.getBody().asString().replaceAll("\\s", ""), "{}", "Expected empty JSON body");
    }
}
