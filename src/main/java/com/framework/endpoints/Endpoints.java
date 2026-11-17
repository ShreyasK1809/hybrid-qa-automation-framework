package com.framework.endpoints;

/** Single source of truth for API paths - a path change is a one-line fix. */
public final class Endpoints {
    public static final String USERS = "/api/users";
    public static final String USER_BY_ID = "/api/users/{id}";
    public static final String LOGIN = "/api/login";

    private Endpoints() { }
}
