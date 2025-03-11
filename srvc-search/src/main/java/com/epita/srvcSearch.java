package com.epita;

import com.epita.ElasticSearch.ElasticSearchRestClient;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/api")
public class srvcSearch {

    @Inject
    ElasticSearchRestClient elasticSearchReastClient;

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public String get() {
        try {
            return elasticSearchReastClient.root();
        } catch (WebApplicationException e) {
            return e.getMessage();
        }
    }


    @GET
    @Path("/get/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String get_index(@PathParam("name") String name) {
        try {
            try {
                elasticSearchReastClient.get_index(name, "");
            } catch (Exception e) {
                elasticSearchReastClient.create_index(name, "{\n" +
                        "\t\"mappings\": {\n" +
                        "\n" +
                        "\t\t\t\"properties\": {\n" +
                        "\t\t\t\t\"movies\": {\n" +
                        "\t\t\t\t\t\"properties\": {\n" +
                        "\t\t\t\t\t\t\"name\": {\n" +
                        "\t\t\t\t\t\t\t\"type\": \"text\"\n" +
                        "\t\t\t\t\t\t},\n" +
                        "\t\t\t\t\t\t\"genres\": {\n" +
                        "\t\t\t\t\t\t\t\"type\": \"text\"\n" +
                        "\t\t\t\t\t\t}\n" +
                        "\t\t\t\t\t}\n" +
                        "\t\t\t\t}\n" +
                        "\n" +
                        "\t\t}\n" +
                        "\t},\n" +
                        "\t\"settings\": {\n" +
                        "\t\t\"number_of_shards\": 1,\n" +
                        "\t\t\"number_of_replicas\": 2\n" +
                        "\t}\n" +
                        "}");
            }
            return elasticSearchReastClient.get_index(name, "");
        } catch (WebApplicationException e) {
            return e.getMessage();
        }
    }

    @GET
    @Path("/delete/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String delete_index(@PathParam("name") String name) {
        try {
            return elasticSearchReastClient.delete_index(name);
        } catch (WebApplicationException e) {
            return e.getMessage();
        }
    }

    @GET
    @Path("/delete/{name}/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String delete_id(@PathParam("name") String name, @PathParam("id") String _id) {
        try {
            return elasticSearchReastClient.delete_id(name, _id);
        } catch (WebApplicationException e) {
            return e.getMessage();
        }
    }

    @GET
    @Path("/add")
    @Produces(MediaType.TEXT_PLAIN)
    public String add() {
        try {
            elasticSearchReastClient.add_one("movies", "{\"name\": \"AAAAAH\", \"genres\" : \"Ok\"}");
            return elasticSearchReastClient.add_bulk("{ \"create\" : { \"_index\" : \"movies\", \"_id\": 1 } }\n" +
                    "{\"name\": \"Titanic\", \"genres\" : \"Snif\"}\n" +
                    "{ \"create\" : { \"_index\" : \"movies\", \"_id\": 2 } }\n" +
                    "{\"name\": \"Mad Max\", \"genres\" : \"BoumBoum\"}\n" +
                    "\n");
        } catch (WebApplicationException e) {
            return e.getMessage();
        }
    }

    @GET
    @Path("/get_match")
    @Produces(MediaType.TEXT_PLAIN)
    public String get_with_match() {
        try {
            return elasticSearchReastClient.get_index("movies", "{\n" +
                    "  \"query\": {\n" +
                    "    \"match\": {\n" +
                    "      \"name\": {\n" +
                    "\t\t\t\t\"query\": \"Titanic\",\n" +
                    "\t\t\t\t\"auto_generate_synonyms_phrase_query\" : false\n" +
                    "\t\t\t}\n" +
                    "    }\n" +
                    "  }\n" +
                    "}");
        } catch (WebApplicationException e) {
            return e.getMessage();
        }
    }
}
