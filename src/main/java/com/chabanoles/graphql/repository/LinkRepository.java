package com.chabanoles.graphql.repository;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.chabanoles.graphql.model.Link;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

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

    public List<Link> getAllLinks() {
        List<Link> allLinks = new ArrayList<>();
        for (Document doc : links.find()){
            allLinks.add(toLink(doc));
        }
        return allLinks;
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
                doc.getString("userId")

        );
    }
}
