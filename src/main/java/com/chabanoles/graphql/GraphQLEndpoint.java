package com.chabanoles.graphql;

import javax.servlet.annotation.WebServlet;

import com.chabanoles.graphql.repository.LinkRepository;
import com.chabanoles.graphql.resolvers.Mutation;
import com.chabanoles.graphql.resolvers.Query;
import com.coxautodev.graphql.tools.SchemaParser;
import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;

/**
 * Created by Nicolas Chabanoles on 08/08/18.
 */
@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {

    public GraphQLEndpoint() {
        super(buildSchema());
    }

    private static GraphQLSchema buildSchema() {
        LinkRepository linkRepository = new LinkRepository();
        return SchemaParser.newParser()
                .file("schema.graphqls")
                .resolvers(new Query(linkRepository), new Mutation(linkRepository))
                .build()
                .makeExecutableSchema();
    }

}
