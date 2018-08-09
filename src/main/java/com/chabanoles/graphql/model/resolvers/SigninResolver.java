package com.chabanoles.graphql.model.resolvers;

import com.chabanoles.graphql.model.SigninPayload;
import com.chabanoles.graphql.model.User;
import com.coxautodev.graphql.tools.GraphQLResolver;

/**
 * Created by Nicolas Chabanoles on 09/08/18.
 */
public class SigninResolver implements GraphQLResolver<SigninPayload>{

    public User user(SigninPayload payload) {
        return payload.getUser();
    }

}
