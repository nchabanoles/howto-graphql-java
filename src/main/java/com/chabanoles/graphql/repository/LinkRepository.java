package com.chabanoles.graphql.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.chabanoles.graphql.model.Link;

/**
 * Created by Nicolas Chabanoles on 08/08/18.
 */
public class LinkRepository {

    private final List<Link> links;

    public LinkRepository() {

        links = new ArrayList<>();
        links.add(new Link("http://github.com/nchabanoles", "my GitHub account"));
        links.add(new Link("http://twitter.com/chabanoles", "my Twitter account"));
    }

    public List<Link> getAllLinks() {
        return Collections.unmodifiableList(links);
    }

    public void saveLink(Link link) {
        links.add(link);
    }
}
