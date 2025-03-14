package com.epita.controller;

import com.epita.contracts.PostsContract;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostRepository implements PanacheMongoRepository<PostsContract>
{

}