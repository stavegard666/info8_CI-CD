package com.epita.controller;

import java.util.function.Consumer;

import org.jboss.logging.Logger;

import com.epita.contracts.PostsContract;
import com.epita.service.UserTimeLineService;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.pubsub.ReactivePubSubCommands;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
@Startup
public class NewPostSubscriber implements Consumer<PostsContract> {
    
    private final ReactivePubSubCommands<PostsContract> subscriber;
    private static final Logger LOG = Logger.getLogger(DeletePostSubscriber.class);

    @Inject
    UserTimeLineService userTimeLineService;

    @Inject
    public NewPostSubscriber(ReactiveRedisDataSource ds) { 
        subscriber = ds.pubsub(PostsContract.class);
        subscriber.subscribe("posts_add_queue").subscribe().with(this);

    }

    @Override
    public void accept(PostsContract postsContract) {
        LOG.info("Accepting Add PostContract result");  
        userTimeLineService.addPostToTimeline(postsContract);
    }


    @PreDestroy
    public void terminate() {
        // subscriber.unsubscribe();
    }
}
