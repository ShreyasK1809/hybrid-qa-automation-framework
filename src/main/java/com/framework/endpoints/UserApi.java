package com.framework.endpoints;

import com.framework.models.UserPayload;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * API service layer: tests call business-named methods and never build raw requests.
 * Returns Response so each test owns its assertions.
 */
public class UserApi {

    private static final Logger log = LogManager.getLogger(UserApi.class);

    public Response getUser(int id) {
        log.info("GET user id={}", id);
        return RestAssured.given().spec(ApiClient.spec())
                .pathParam("id", id)
                .log().ifValidationFails()
                .when().get(Endpoints.USER_BY_ID);
    }

    public Response createUser(UserPayload payload) {
        log.info("POST user {}", payload);
        return RestAssured.given().spec(ApiClient.spec())
                .body(payload)
                .log().ifValidationFails()
                .when().post(Endpoints.USERS);
    }

    public Response login(Map<String, String> credentials) {
        log.info("POST login for {}", credentials.get("email"));
        return RestAssured.given().spec(ApiClient.spec())
                .body(credentials)
                .log().ifValidationFails()
                .when().post(Endpoints.LOGIN);
    }
}
