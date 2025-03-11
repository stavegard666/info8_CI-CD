package com.epita.ElasticSearch;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class ElasticSearchRestClient {

    private String URI = "http://localhost:9200";

    private final Client client;

    public ElasticSearchRestClient() {
        this.client = ClientBuilder.newClient();
    }

    public String root() {
        try{
            Response response = client.target(URI).path("/").request(MediaType.TEXT_PLAIN).get();
            if (response.getStatus() == 200) {
                return response.readEntity(String.class);
            } else {
                throw new WebApplicationException("Can't get hello", response.getStatus());
            }
        }
        catch (WebApplicationException e) {
            throw new WebApplicationException(e.getMessage(), e.getResponse().getStatus());
        }
    }

    public String create_index(String index_name, String body) {
        try{
            Response response = client.target(URI).path(String.format("/%s", index_name)).request(MediaType.TEXT_PLAIN).put(Entity.json(body));
            if (response.getStatus() == 200) {
                return response.readEntity(String.class);
            } else {
                throw new WebApplicationException("Can't create index", response.getStatus());
            }
        }
        catch (WebApplicationException e) {
            throw new WebApplicationException(e.getMessage(), e.getResponse().getStatus());
        }
    }
    public String get_index(String index_name, String body) {
        try{
            Response response = client.target(URI).path(String.format("/%s/_search", index_name)).request(MediaType.TEXT_PLAIN).post(Entity.json(body));
            if (response.getStatus() == 200) {
                return response.readEntity(String.class);
            } else {
                throw new WebApplicationException("Can't get index", response.getStatus());
            }
        }
        catch (WebApplicationException e) {
            throw new WebApplicationException(e.getMessage(), e.getResponse().getStatus());
        }
    }
    public String add_bulk(String body) {
        try{
            Response response = client.target(URI).path("/_bulk").request(MediaType.TEXT_PLAIN).post(Entity.json(body));
            if (response.getStatus() == 200) {
                return response.readEntity(String.class);
            } else {
                throw new WebApplicationException("Can't add bulk", response.getStatus());
            }
        }
        catch (WebApplicationException e) {
            throw new WebApplicationException(e.getMessage(), e.getResponse().getStatus());
        }
    }

    public String add_one(String index_name, String body) {
        try{
            Response response = client.target(URI).path(String.format("/%s/_doc", index_name)).request(MediaType.TEXT_PLAIN).post(Entity.json(body));
            if (response.getStatus() == 200 || response.getStatus() == 201) {
                return response.readEntity(String.class);
            } else {
                throw new WebApplicationException("Can't add one", response.getStatus());
            }
        }
        catch (WebApplicationException e) {
            throw new WebApplicationException(e.getMessage(), e.getResponse().getStatus());
        }
    }
    public String delete_id(String index_name, String id) {
        try{
            Response response = client.target(URI).path(String.format("/%s/_doc/%s", index_name, id)).request(MediaType.TEXT_PLAIN).delete();
            if (response.getStatus() == 200 || response.getStatus() == 201) {
                return response.readEntity(String.class);
            } else {
                throw new WebApplicationException("Can't delete one", response.getStatus());
            }
        }
        catch (WebApplicationException e) {
            throw new WebApplicationException(e.getMessage(), e.getResponse().getStatus());
        }
    }
    public String delete_index(String index_name) {
        try{
            Response response = client.target(URI).path(String.format("/%s/", index_name)).request(MediaType.TEXT_PLAIN).delete();
            if (response.getStatus() == 200 || response.getStatus() == 201) {
                return response.readEntity(String.class);
            } else {
                throw new WebApplicationException("Can't delete one", response.getStatus());
            }
        }
        catch (WebApplicationException e) {
            throw new WebApplicationException(e.getMessage(), e.getResponse().getStatus());
        }
    }
}

