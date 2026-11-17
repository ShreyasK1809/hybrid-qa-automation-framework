package com.framework.models;

/** Request body POJO (Java record). Jackson serialises it automatically for REST Assured. */
public record UserPayload(String name, String job) { }
