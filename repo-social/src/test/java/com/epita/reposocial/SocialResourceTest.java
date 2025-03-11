package com.epita.reposocial;

import com.epita.reposocial.controller.SocialController;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class SocialResourceTest {

    private static final String USER1 = "user1";
    private static final String USER2 = "user2";
    private static final String POST1 = "post1";

    @Test
    void testLikePost() {
        // Test like d'un post
        given()
            .when()
            .post("/social/likes/" + USER1 + "/" + POST1)
            .then()
            .statusCode(201)
            .body(notNullValue());

        // Vérifier que le like existe
        given()
            .when()
            .get("/social/likes/post/" + POST1)
            .then()
            .statusCode(200)
            .body("$", is(notNullValue()))
            .body("$.size()", is(1))
            .body("[0]", is(USER1));
    }

    @Test
    void testUnlikePost() {
        // Créer un like
        given()
            .when()
            .post("/social/likes/" + USER1 + "/" + POST1)
            .then()
            .statusCode(201);

        // Test unlike
        given()
            .when()
            .delete("/social/likes/" + USER1 + "/" + POST1)
            .then()
            .statusCode(204);

        // Vérifier que le like n'existe plus
        given()
            .when()
            .get("/social/likes/post/" + POST1)
            .then()
            .statusCode(200)
            .body("$.size()", is(0));
    }

    @Test
    void testFollowUser() {
        // Test follow d'un utilisateur
        given()
            .when()
            .post("/social/follows/" + USER1 + "/" + USER2)
            .then()
            .statusCode(201)
            .body(notNullValue());

        // Vérifier que le follow existe
        given()
            .when()
            .get("/social/follows/followers/" + USER2)
            .then()
            .statusCode(200)
            .body("$", is(notNullValue()))
            .body("$.size()", is(1))
            .body("[0]", is(USER1));
    }

    @Test
    void testUnfollowUser() {
        // Créer un follow
        given()
            .when()
            .post("/social/follows/" + USER1 + "/" + USER2)
            .then()
            .statusCode(201);

        // Test unfollow
        given()
            .when()
            .delete("/social/follows/" + USER1 + "/" + USER2)
            .then()
            .statusCode(204);

        // Vérifier que le follow n'existe plus
        given()
            .when()
            .get("/social/follows/followers/" + USER2)
            .then()
            .statusCode(200)
            .body("$.size()", is(0));
    }

    @Test
    void testBlockUser() {
        // Test block d'un utilisateur
        given()
            .when()
            .post("/social/blocks/" + USER1 + "/" + USER2)
            .then()
            .statusCode(201)
            .body(notNullValue());

        // Vérifier que le block existe
        given()
            .when()
            .get("/social/blocks/blocked/" + USER1)
            .then()
            .statusCode(200)
            .body("$", is(notNullValue()))
            .body("$.size()", is(1))
            .body("[0]", is(USER2));
    }

    @Test
    void testUnblockUser() {
        // Créer un block
        given()
            .when()
            .post("/social/blocks/" + USER1 + "/" + USER2)
            .then()
            .statusCode(201);

        // Test unblock
        given()
            .when()
            .delete("/social/blocks/" + USER1 + "/" + USER2)
            .then()
            .statusCode(204);

        // Vérifier que le block n'existe plus
        given()
            .when()
            .get("/social/blocks/blocked/" + USER1)
            .then()
            .statusCode(200)
            .body("$.size()", is(0));
    }

    @Test
    void testBlockRemovesFollows() {
        // Créer des follows
        given()
            .when()
            .post("/social/follows/" + USER1 + "/" + USER2)
            .then()
            .statusCode(201);

        given()
            .when()
            .post("/social/follows/" + USER2 + "/" + USER1)
            .then()
            .statusCode(201);

        // Bloquer un utilisateur
        given()
            .when()
            .post("/social/blocks/" + USER1 + "/" + USER2)
            .then()
            .statusCode(201);

        // Vérifier que les follows ont été supprimés
        given()
            .when()
            .get("/social/follows/following/" + USER1)
            .then()
            .statusCode(200)
            .body("$.size()", is(0));

        given()
            .when()
            .get("/social/follows/following/" + USER2)
            .then()
            .statusCode(200)
            .body("$.size()", is(0));
    }

    @Test
    void testCannotFollowBlockedUser() {
        // Créer un block
        given()
            .when()
            .post("/social/blocks/" + USER1 + "/" + USER2)
            .then()
            .statusCode(201);

        // Tenter de suivre l'utilisateur bloqué
        given()
            .when()
            .post("/social/follows/" + USER2 + "/" + USER1)
            .then()
            .statusCode(403);
    }
} 