package com.epita.controller;

import java.util.function.Consumer;

import org.jboss.logging.Logger;

import com.epita.contracts.PostsContract;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.pubsub.ReactivePubSubCommands;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@Startup
@ApplicationScoped
public class DeletePostSubscriber implements Consumer<PostsContract> {


    private final ReactivePubSubCommands<PostsContract> subscriber;
    private static final Logger LOG = Logger.getLogger(DeletePostSubscriber.class);


    @Inject
    public DeletePostSubscriber(ReactiveRedisDataSource ds) { 
        subscriber = ds.pubsub(PostsContract.class);
        subscriber.subscribe("posts_delete_queue").subscribe().with(this);

    }

    @Override
    public void accept(PostsContract postsContract) {
        LOG.info("Accepting Delete PostContract result");  
    }


    @PreDestroy
    public void terminate() {
        // subscriber.unsubscribe();
    }
}
