package com.chabanoles.graphql.repository;

import static com.mongodb.client.model.Filters.eq;

import com.chabanoles.graphql.model.User;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Created by Nicolas Chabanoles on 09/08/18.
 */
public class UserRepository {

    private final MongoCollection<Document> users;

    public UserRepository(MongoCollection<Document> users) {
        this.users = users;
    }

    public User findByEmail(String email) {
        Document doc = users.find(eq("email", email)).first();
        return toUser(doc);
    }

    public User findById(String id) {
        Document doc = users.find(eq("_id", new ObjectId(id))).first();
        return toUser(doc);
    }

    public User saveUser(User user) {
        Document doc = new Document();
        doc.append("name",user.getName());
        doc.append("email",user.getEmail());
        doc.append("password", user.getPassword());
        users.insertOne(doc);
        return new User(
                doc.get("_id").toString(),
                user.getName(),
                user.getEmail(),
                user.getPassword()
        );
    }

    private User toUser(Document doc) {
        if(doc == null) {
            return null;
        }
        return new User(
                doc.get("_id").toString(),
                doc.getString("name"),
                doc.getString("email"),
                doc.getString("password")
        );
    }
}
