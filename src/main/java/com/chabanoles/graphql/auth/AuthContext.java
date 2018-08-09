package com.chabanoles.graphql.auth;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chabanoles.graphql.model.User;
import graphql.servlet.GraphQLContext;

/**
 * Created by Nicolas Chabanoles on 09/08/18.
 */
public class AuthContext extends GraphQLContext {

    private final User user;

    public AuthContext(User user, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        super(request, response);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
