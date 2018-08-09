package com.chabanoles.graphql.rootresolvers;

import java.util.List;

import com.chabanoles.graphql.model.Link;
import com.chabanoles.graphql.repository.LinkRepository;
import com.coxautodev.graphql.tools.GraphQLRootResolver;

/**
 * Created by Nicolas Chabanoles on 08/08/18.
 */
public class Query implements GraphQLRootResolver {

    private final LinkRepository linkRepository;

    public Query(LinkRepository linkRepository){
        this.linkRepository = linkRepository;
    }

    public List<Link> allLinks() {
        return linkRepository.getAllLinks();
    }
}
