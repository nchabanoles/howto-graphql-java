package com.chabanoles.graphql.resolvers;

import com.chabanoles.graphql.model.Link;
import com.chabanoles.graphql.repository.LinkRepository;
import com.coxautodev.graphql.tools.GraphQLRootResolver;

/**
 * Created by Nicolas Chabanoles on 08/08/18.
 */
public class Mutation implements GraphQLRootResolver {

    private LinkRepository linkRepository;

    public Mutation(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public Link createLink(String url, String description) {
        Link newLink = new Link(url, description);
        linkRepository.saveLink(newLink);
        return newLink;
    }
}
