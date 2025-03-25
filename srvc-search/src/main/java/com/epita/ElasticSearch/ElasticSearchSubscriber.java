package com.epita.ElasticSearch;

import java.io.IOException;
import java.util.function.Consumer;

import com.epita.ElasticSearch.contracts.PostContract;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.pubsub.PubSubCommands;
import io.quarkus.runtime.Startup;
import io.vertx.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Startup
@ApplicationScoped
public class ElasticSearchSubscriber implements Consumer<PostContract> {
    
    private final PubSubCommands.RedisSubscriber subscriber;

    @Inject
    ElasticSearchRestClient elasticSearchRestClient;

    @Inject
    Vertx vertx;

    public ElasticSearchSubscriber(RedisDataSource ds) {
        this.subscriber = ds.pubsub(PostContract.class).subscribe("posts", this::accept);
    }

    @Override
    public void accept(PostContract post) {
        vertx.executeBlocking(future -> {
            try {
                elasticSearchRestClient.insert_post(post);
                future.complete();
            } catch (IOException e) {
                future.fail(e);
            }
        });
    }

    public void close() {
        subscriber.unsubscribe();
    }
}
