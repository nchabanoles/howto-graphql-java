package com.chabanoles.graphql.model.resolvers;

import com.chabanoles.graphql.model.Link;
import com.chabanoles.graphql.model.User;
import com.chabanoles.graphql.model.Vote;
import com.chabanoles.graphql.repository.LinkRepository;
import com.chabanoles.graphql.repository.UserRepository;
import com.coxautodev.graphql.tools.GraphQLResolver;

/**
 * Created by Nicolas Chabanoles on 09/08/18.
 */
public class VoteResolver implements GraphQLResolver<Vote> {
    private final LinkRepository linkRepository;
    private final UserRepository userRepository;

    public VoteResolver(LinkRepository linkRepository, UserRepository userRepository) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
    }

    public Link link(Vote vote){
        if(vote.getLinkId() == null) {
            return null;
        }
        return linkRepository.findById(vote.getLinkId());
    }

    public User user(Vote vote) {
        if(vote.getUserId() == null) {
            return null;
        }
        return userRepository.findById(vote.getUserId());
    }
}
