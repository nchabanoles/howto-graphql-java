package com.chabanoles.graphql;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chabanoles.graphql.auth.AuthContext;
import com.chabanoles.graphql.error.SanitizedError;
import com.chabanoles.graphql.model.User;
import com.chabanoles.graphql.model.resolvers.LinkResolver;
import com.chabanoles.graphql.model.resolvers.SigninResolver;
import com.chabanoles.graphql.model.resolvers.VoteResolver;
import com.chabanoles.graphql.repository.LinkRepository;
import com.chabanoles.graphql.repository.UserRepository;
import com.chabanoles.graphql.repository.VoteRepository;
import com.chabanoles.graphql.rootresolvers.Mutation;
import com.chabanoles.graphql.rootresolvers.Query;
import com.chabanoles.graphql.scalar.Scalars;
import com.coxautodev.graphql.tools.SchemaParser;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.schema.GraphQLSchema;
import graphql.servlet.GraphQLContext;
import graphql.servlet.SimpleGraphQLServlet;

/**
 * Created by Nicolas Chabanoles on 08/08/18.
 */
@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {

    private static final LinkRepository linkRepository;
    private static final UserRepository userRepository;
    private static final VoteRepository voteRepository;

    static {
        MongoDatabase mongo = new MongoClient(new MongoClientURI("mongodb://mongouser:mongopassword1@ds217002.mlab.com:17002/graphql-java")).getDatabase("graphql-java");
        linkRepository = new LinkRepository(mongo.getCollection("links"));
        userRepository = new UserRepository(mongo.getCollection("users"));
        voteRepository = new VoteRepository(mongo.getCollection("votes"));
    }

    public GraphQLEndpoint() {
        super(buildSchema());
    }

    @Override
    protected GraphQLContext createContext(Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        User user = request.map(req -> req.getHeader("Authorization"))
                .filter(id -> !id.isEmpty())
                .map(id -> id.replace("Bearer ", ""))
                .map(userRepository::findById)
                .orElse(null);
        return new AuthContext(user, request, response);
    }

    @Override
    protected List<GraphQLError> filterGraphQLErrors(List<GraphQLError> errors) {
        return errors.stream()
                .filter(e -> e instanceof ExceptionWhileDataFetching || super.isClientError(e))
                .map(e -> e instanceof ExceptionWhileDataFetching ? new SanitizedError((ExceptionWhileDataFetching) e) :e)
                .collect(Collectors.toList());
    }

    private static GraphQLSchema buildSchema() {

        return SchemaParser.newParser()
                .file("schema.graphqls")
                .resolvers(
                        new Query(linkRepository),
                        new Mutation(linkRepository, userRepository, voteRepository),
                        new SigninResolver(),
                        new LinkResolver(userRepository),
                        new VoteResolver(linkRepository,userRepository)
                )
                .scalars(Scalars.dateTime)
                .build()
                .makeExecutableSchema();
    }

}
