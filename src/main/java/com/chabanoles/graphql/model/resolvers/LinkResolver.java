package com.chabanoles.graphql.model.resolvers;

import com.chabanoles.graphql.model.Link;
import com.chabanoles.graphql.model.User;
import com.chabanoles.graphql.repository.UserRepository;
import com.coxautodev.graphql.tools.GraphQLResolver;

/**
 * Created by Nicolas Chabanoles on 09/08/18.
 */
public class LinkResolver implements GraphQLResolver<Link>{

    private final UserRepository userRepository;

    public LinkResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User postedBy(Link link) {
        if(link.getUserId() == null) {
            return null;
        }
        return userRepository.findById(link.getUserId());
    }
}
