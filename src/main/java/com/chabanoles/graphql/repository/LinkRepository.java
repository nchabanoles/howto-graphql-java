package com.chabanoles.graphql.repository;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.chabanoles.graphql.model.Link;
import com.chabanoles.graphql.model.LinkFilter;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Nicolas Chabanoles on 08/08/18.
 */
public class LinkRepository {

    private final MongoCollection<Document> links;

    public LinkRepository(MongoCollection<Document> links) {
        this.links = links;
    }

    public Link findById(String id) {
        Document doc = links.find(eq("_id", new ObjectId(id))).first();
        return toLink(doc);
    }

    public List<Link> getAllLinks(LinkFilter filter, int skip, int first) {
        Optional<Bson> mongoFilter = Optional.ofNullable(filter).map(this::buildFilter);

        List<Link> allLinks = new ArrayList<>();
        FindIterable<Document> documents = mongoFilter.map(links::find).orElseGet(links::find);
        for (Document doc : documents.skip(skip).limit(first)){
            allLinks.add(toLink(doc));
        }
        return allLinks;
    }

    @Nullable
    private Bson buildFilter(LinkFilter filter) {
        String descriptionPattern = filter.getDescriptionContains();
        String urlPattern = filter.getUrlContains();
        Bson descriptionCondition = null;
        Bson urlCondition = null;
        if(descriptionPattern!=null && !descriptionPattern.isEmpty()) {
            descriptionCondition = regex("description", ".*" + descriptionPattern + ".*", "i");
        }
        if(urlPattern!=null && !urlPattern.isEmpty()) {
            urlCondition = regex("url", ".*" + urlPattern + ".*");
        }
        if(descriptionCondition != null && urlCondition!= null) {
            return and(descriptionCondition, urlCondition);
        }
        return descriptionCondition != null? descriptionCondition : urlCondition;
    }

    public void saveLink(Link link) {
        Document doc = new Document();
        doc.append("url", link.getUrl());
        doc.append("description", link.getDescription());
        doc.append("postedBy", link.getUserId());
        links.insertOne(doc);
    }

    private Link toLink(Document doc) {
        return new Link(
                doc.get("_id").toString(),
                doc.getString("url"),
                doc.getString("description"),
                doc.getString("postedBy")
        );
    }
}
