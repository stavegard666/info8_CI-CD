package com.epita.repository;

import com.epita.contracts.PostsContract;
import io.quarkus.redis.datasource.RedisDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.redis.datasource.pubsub.PubSubCommands;

@ApplicationScoped
public class DeletePublisher {

    private final PubSubCommands<PostsContract> publisher;

    public DeletePublisher(final RedisDataSource ds) {
        publisher = ds.pubsub(PostsContract.class);
    }

    public void publish(PostsContract post)
    {
        publisher.publish("posts_delete_queue", post);
    }
}
