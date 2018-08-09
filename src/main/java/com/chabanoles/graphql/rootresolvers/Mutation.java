package com.chabanoles.graphql.rootresolvers;

import com.chabanoles.graphql.model.Link;
import com.chabanoles.graphql.model.SigninPayload;
import com.chabanoles.graphql.model.User;
import com.chabanoles.graphql.model.input.AuthData;
import com.chabanoles.graphql.repository.LinkRepository;
import com.chabanoles.graphql.repository.UserRepository;
import com.coxautodev.graphql.tools.GraphQLRootResolver;
import graphql.GraphQLException;

/**
 * Created by Nicolas Chabanoles on 08/08/18.
 */
public class Mutation implements GraphQLRootResolver {

    private LinkRepository linkRepository;
    private UserRepository userRepository;

    public Mutation(LinkRepository linkRepository, UserRepository userRepository) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
    }

    public Link createLink(String url, String description) {
        Link newLink = new Link(url, description);
        linkRepository.saveLink(newLink);
        return newLink;
    }

    public User createUser(String name, AuthData authData) {
        User user = new User(name, authData.getEmail(), authData.getPassword());
        return userRepository.saveUser(user);
    }

    public SigninPayload signinUser(AuthData auth) throws IllegalAccessException {
        User user = userRepository.findByEmail(auth.getEmail());
        if (user == null || !user.getPassword().equals(auth.getPassword())) {
            throw new IllegalAccessException("Invalid credentials");
        }
        return new SigninPayload(user.getId(), user); //Instead of using the user id as token, I should generate a JWT token

    }
}
