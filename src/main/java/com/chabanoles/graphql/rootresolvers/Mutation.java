package com.chabanoles.graphql.rootresolvers;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import com.chabanoles.graphql.auth.AuthContext;
import com.chabanoles.graphql.model.Link;
import com.chabanoles.graphql.model.SigninPayload;
import com.chabanoles.graphql.model.User;
import com.chabanoles.graphql.model.Vote;
import com.chabanoles.graphql.model.input.AuthData;
import com.chabanoles.graphql.repository.LinkRepository;
import com.chabanoles.graphql.repository.UserRepository;
import com.chabanoles.graphql.repository.VoteRepository;
import com.coxautodev.graphql.tools.GraphQLRootResolver;
import graphql.schema.DataFetchingEnvironment;

/**
 * Created by Nicolas Chabanoles on 08/08/18.
 */
public class Mutation implements GraphQLRootResolver {

    private LinkRepository linkRepository;
    private UserRepository userRepository;
    private VoteRepository voteRepository;

    public Mutation(LinkRepository linkRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public Link createLink(String url, String description, DataFetchingEnvironment env) {
        AuthContext context = env.getContext();
        Link newLink = new Link(url, description, context.getUser().getId());
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

    //createVote(linkId: ID, userId: ID): Vote
    public Vote createVote(String linkId, String userId) {
        ZonedDateTime now = Instant.now().atZone(ZoneOffset.UTC);
        Vote vote = new Vote(now, userId, linkId);
        return voteRepository.saveVote(vote);
    }

}
