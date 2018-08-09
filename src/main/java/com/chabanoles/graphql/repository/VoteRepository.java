package com.chabanoles.graphql.repository;

import static com.mongodb.client.model.Filters.eq;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.chabanoles.graphql.model.Vote;
import com.chabanoles.graphql.scalar.Scalars;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 * Created by Nicolas Chabanoles on 09/08/18.
 */
public class VoteRepository {
    MongoCollection<Document> votes;

    public VoteRepository(MongoCollection<Document> votes) {
        this.votes = votes;
    }

    public List<Vote> findByUserId(String userId) {
        List<Vote> results = new ArrayList<>();
        votes.find(eq("userId", userId)).forEach((Consumer<? super Document>)  document -> results.add(toVote(document)));
        return results;
    }

    public List<Vote> findByLinkId(String linkId) {
        List<Vote> results = new ArrayList<>();
        votes.find(eq("linkId", linkId)).forEach((Consumer<? super Document>)  document -> results.add(toVote(document)));
        return results;
    }

    public Vote saveVote(Vote vote) {
        Document doc = new Document();
        doc.append("createdAt", Scalars.dateTime.getCoercing().serialize(vote.getCreatedAt()));
        doc.append("userId", vote.getUserId());
        doc.append("linkId", vote.getLinkId());
        votes.insertOne(doc);
        return toVote(doc);
    }

    private Vote toVote(Document doc) {
        return new Vote(doc.get("_id").toString(),
                ZonedDateTime.parse(doc.getString("createdAt")),
                doc.getString("userId"),
                doc.getString("linkId")
                );
    }
}
