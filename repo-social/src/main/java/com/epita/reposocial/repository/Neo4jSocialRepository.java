package com.epita.reposocial.repository;

import org.neo4j.driver.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class Neo4jSocialRepository {
    private static final Logger LOG = Logger.getLogger(Neo4jSocialRepository.class);

    private final Driver driver;

    public Neo4jSocialRepository() {
        this.driver = GraphDatabase.driver(
                "bolt://localhost:7687",
                AuthTokens.basic("neo4j", "password"));
    }

    // Follow operations
    public void createFollow(UUID followerId, UUID followedId) {
        String query = """
                MERGE (follower:User {id: $followerId})
                MERGE (followed:User {id: $followedId})
                MERGE (follower)-[r:FOLLOWS]->(followed)
                SET r.createdAt = datetime()
                """;

        try (Session session = driver.session()) {
            session.run(query, Values.parameters(
                    "followerId", followerId.toString(),
                    "followedId", followedId.toString()));
        }
    }

    public void deleteFollow(UUID followerId, UUID followedId) {
        String query = """
                MATCH (follower:User {id: $followerId})-[r:FOLLOWS]->(followed:User {id: $followedId})
                DELETE r
                """;

        try (Session session = driver.session()) {
            session.run(query, Values.parameters(
                    "followerId", followerId.toString(),
                    "followedId", followedId.toString()));
        }
    }

    public List<UUID> getFollowers(UUID userId) {
        String query = """
                MATCH (user:User {id: $userId})<-[r:FOLLOWS]-(follower:User)
                RETURN follower.id as id
                """;

        try (Session session = driver.session()) {
            return session.run(query, Values.parameters("userId", userId.toString()))
                    .stream()
                    .map(record -> UUID.fromString(record.get("id").asString()))
                    .collect(Collectors.toList());
        }
    }

    public List<UUID> getFollowing(UUID userId) {
        String query = """
                MATCH (user:User {id: $userId})-[r:FOLLOWS]->(followed:User)
                RETURN followed.id as id
                """;

        try (Session session = driver.session()) {
            return session.run(query, Values.parameters("userId", userId.toString()))
                    .stream()
                    .map(record -> UUID.fromString(record.get("id").asString()))
                    .collect(Collectors.toList());
        }
    }

    // Block operations
    public void createBlock(UUID blockerId, UUID blockedId) {
        String query = """
                MERGE (blocker:User {id: $blockerId})
                MERGE (blocked:User {id: $blockedId})
                MERGE (blocker)-[r:BLOCKS]->(blocked)
                SET r.createdAt = datetime()
                """;

        try (Session session = driver.session()) {
            session.run(query, Values.parameters(
                    "blockerId", blockerId.toString(),
                    "blockedId", blockedId.toString()));
        }
    }

    public void deleteBlock(UUID blockerId, UUID blockedId) {
        String query = """
                MATCH (blocker:User {id: $blockerId})-[r:BLOCKS]->(blocked:User {id: $blockedId})
                DELETE r
                """;

        try (Session session = driver.session()) {
            session.run(query, Values.parameters(
                    "blockerId", blockerId.toString(),
                    "blockedId", blockedId.toString()));
        }
    }

    public List<UUID> getBlockedUsers(UUID userId) {
        String query = """
                MATCH (user:User {id: $userId})-[r:BLOCKS]->(blocked:User)
                RETURN blocked.id as id
                """;

        try (Session session = driver.session()) {
            return session.run(query, Values.parameters("userId", userId.toString()))
                    .stream()
                    .map(record -> UUID.fromString(record.get("id").asString()))
                    .collect(Collectors.toList());
        }
    }

    public List<UUID> getBlockingUsers(UUID userId) {
        String query = """
                MATCH (user:User {id: $userId})<-[r:BLOCKS]-(blocker:User)
                RETURN blocker.id as id
                """;

        try (Session session = driver.session()) {
            return session.run(query, Values.parameters("userId", userId.toString()))
                    .stream()
                    .map(record -> UUID.fromString(record.get("id").asString()))
                    .collect(Collectors.toList());
        }
    }

    // Social distance operations
    public int getSocialDistance(UUID user1Id, UUID user2Id) {
        String query = """
                MATCH path = shortestPath(
                    (user1:User {id: $user1Id})-[*]-(user2:User {id: $user2Id})
                )
                RETURN length(path) as distance
                """;

        try (Session session = driver.session()) {
            var result = session.run(query, Values.parameters(
                    "user1Id", user1Id.toString(),
                    "user2Id", user2Id.toString()));

            if (result.hasNext()) {
                return result.next().get("distance").asInt();
            }
            return -1; // -1 means no path found
        }
    }

    public List<UUID> getUsersAtDistance(UUID userId, int distance) {
        String query = """
                MATCH (user:User {id: $userId})
                MATCH path = (user)-[*1..$distance]-(other:User)
                WHERE length(path) = $distance
                RETURN other.id as id
                """;

        try (Session session = driver.session()) {
            return session.run(query, Values.parameters(
                    "userId", userId.toString(),
                    "distance", distance))
                    .stream()
                    .map(record -> UUID.fromString(record.get("id").asString()))
                    .collect(Collectors.toList());
        }
    }
}