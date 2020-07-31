package io.leangen.spqr.samples.demo.controller;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaPrinter;
import io.leangen.graphql.GraphQLSchemaGenerator;
import io.leangen.spqr.samples.demo.query.annotated.PersonQuery;
import io.leangen.spqr.samples.demo.query.annotated.SocialNetworkQuery;
import io.leangen.spqr.samples.demo.query.annotated.VendorQuery;
import io.leangen.spqr.samples.demo.query.unannotated.DomainQuery;
import io.leangen.spqr.samples.demo.query.unannotated.ProductQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class GraphQLSampleController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GraphQLSampleController.class);

    private final GraphQL graphQL;
    private final GraphQLSchema schema;

    @Autowired
    public GraphQLSampleController(PersonQuery personQuery,
                                   SocialNetworkQuery socialNetworkQuery,
                                   DomainQuery domainQuery,
                                   ProductQuery productQuery,
                                   VendorQuery vendorQuery) {

        //Schema generated from query classes
        schema = new GraphQLSchemaGenerator()
                .withBasePackages("io.leangen.spqr.samples.demo")
                .withOperationsFromSingletons(personQuery, socialNetworkQuery, vendorQuery, domainQuery, productQuery)
                .generate();
        graphQL = GraphQL.newGraphQL(schema).build();

        LOGGER.info("Generated GraphQL schema using SPQR");
    }

    @PostMapping(value = "/graphql", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> indexFromAnnotated(@RequestBody Map<String, String> request, HttpServletRequest raw) {
        final ExecutionResult executionResult = graphQL.execute(ExecutionInput.newExecutionInput()
                .query(request.get("query"))
                .operationName(request.get("operationName"))
                .context(raw)
                .build());
        return executionResult.toSpecification();
    }

    @GetMapping(value = "/graphql")
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
