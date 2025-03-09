#!/bin/bash

docker compose up -d mongodb

sleep 1

docker compose exec mongodb mongosh --eval "db.createCollection('users')" tinyX
docker compose exec mongodb mongosh --eval "db.createCollection('posts')" tinyX
docker compose exec mongodb mongosh --eval "db.createCollection('likes')" tinyX
docker compose exec mongodb mongosh --eval "db.createCollection('follows')" tinyX
docker compose exec mongodb mongosh --eval "db.createCollection('blocks')" tinyX





