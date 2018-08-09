package com.chabanoles.graphql.model;

/**
 * Created by Nicolas Chabanoles on 09/08/18.
 */
public class SigninPayload {
    private final String token;
    private final User user;

    public SigninPayload(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
