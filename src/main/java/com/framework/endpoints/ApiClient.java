package com.framework.endpoints;

import com.framework.config.ConfigReader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/** Builds the shared RequestSpecification (base URI, content type, auth header). */
public final class ApiClient {

    private ApiClient() { }

    public static RequestSpecification spec() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(ConfigReader.get("api.base.url"))
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON);

        String apiKey = ConfigReader.get("reqres.api.key", "");
        if (!apiKey.isBlank()) {
            builder.addHeader("x-api-key", apiKey);
        }
        return builder.build();
    }
}
