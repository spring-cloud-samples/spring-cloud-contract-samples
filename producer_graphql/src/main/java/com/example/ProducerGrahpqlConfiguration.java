package com.example;

import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLTypeReference;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static graphql.Scalars.GraphQLString;

@Configuration
public class ProducerGrahpqlConfiguration {

	@Bean
	public GraphQLSchema schema(PersonToCheckDataFetcher personToCheckDataFetcher,
			PeopleToCheckDataFetcher peopleToCheckDataFetcher) {
		return GraphQLSchema.newSchema()
				.query(buildCustomerType(personToCheckDataFetcher, peopleToCheckDataFetcher))
				.build();
	}

	private GraphQLObjectType buildCustomerType(PersonToCheckDataFetcher personToCheckDataFetcher,
			PeopleToCheckDataFetcher peopleToCheckDataFetcher) {
		GraphQLObjectType.Builder builder = GraphQLObjectType.newObject()
				.name("Query")
				.field(personToCheckDefinition(personToCheckDataFetcher))
				.field(peopleToCheckDefinition(peopleToCheckDataFetcher));
		return builder.build();
	}

	private GraphQLFieldDefinition personToCheckDefinition(PersonToCheckDataFetcher personToCheckDataFetcher) {
		return GraphQLFieldDefinition.newFieldDefinition()
				.name("personToCheck")
				.argument(GraphQLArgument.newArgument()
						.name("name")
						.type(new GraphQLNonNull(GraphQLString)))
				.type(new GraphQLNonNull(GraphQLObjectType.newObject()
						.name(PersonToCheck.class.getSimpleName())
						.field(GraphQLFieldDefinition.newFieldDefinition()
								.name("name")
								.type(new GraphQLNonNull(GraphQLString))
								.build())
						.field(GraphQLFieldDefinition.newFieldDefinition()
								.name("age")
								.type(new GraphQLNonNull(GraphQLString))
								.build())
						.build()))
				.dataFetcher(personToCheckDataFetcher)
				.build();
	}


	private GraphQLFieldDefinition peopleToCheckDefinition(PeopleToCheckDataFetcher peopleToCheckDataFetcher) {
		GraphQLFieldDefinition.Builder builder = GraphQLFieldDefinition.newFieldDefinition()
				.name("peopleToCheck")
				.type(new GraphQLNonNull(new GraphQLList(new GraphQLTypeReference(PersonToCheck.class.getSimpleName()))))
				.dataFetcher(peopleToCheckDataFetcher);
		return builder.build();
	}
}
