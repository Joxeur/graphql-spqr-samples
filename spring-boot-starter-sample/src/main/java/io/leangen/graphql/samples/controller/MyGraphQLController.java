package io.leangen.graphql.samples.controller;

import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyGraphQLController {

    private final GraphQLSchema schema;

    @Autowired
    public MyGraphQLController(GraphQLSchema schema) {
        this.schema = schema;
    }

    @GetMapping(value = "/my-graphql")
    @ResponseBody
    public String getSchema() {
        return new SchemaPrinter(
            // Tweak the options accordingly
            SchemaPrinter.Options.defaultOptions()
                    .includeScalarTypes(true)
                    .includeExtendedScalarTypes(true)
                    .includeIntrospectionTypes(true)
                    .includeSchemaDefintion(true)
        ).print(schema);
    }
}
