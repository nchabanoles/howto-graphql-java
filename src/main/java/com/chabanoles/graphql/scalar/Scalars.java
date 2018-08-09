package com.chabanoles.graphql.scalar;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;

/**
 * Created by Nicolas Chabanoles on 09/08/18.
 */
public class Scalars {

    public static GraphQLScalarType dateTime = new GraphQLScalarType("DateTime", "DateTime scalar", new Coercing() {
        @Override
        public String serialize(Object input) {
            return ((ZonedDateTime)input).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }

        @Override
        public Object parseValue(Object input) {
            return serialize(input);
        }

        @Override
        public Object parseLiteral(Object input) {
            if(input instanceof StringValue) {
                return ZonedDateTime.parse(((StringValue)input).getValue());
            } else {
                return null;
            }
        }
    });
}
