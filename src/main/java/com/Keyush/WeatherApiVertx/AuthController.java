package com.Keyush.WeatherApiVertx;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.core.json.JsonObject;
import io.ebean.Ebean;
import io.ebean.Query;
import io.ebean.ExpressionList;

public class AuthController {
  private final Vertx vertx;
  private final JWTAuthProvider jwtAuthProvider;

  public AuthController(Vertx vertx, Router router) {
    this.vertx = vertx;
    this.jwtAuthProvider = new JWTAuthProvider();
    router.post("/login").handler(this::login);
  }

  private void login(RoutingContext context) {
    JsonObject body = context.getBodyAsJson();
    if (body == null) {
      context.response()
        .setStatusCode(400)
        .end("Invalid JSON format");
      return;
    }

    String email = body.getString("email");
    String password = body.getString("password");

    if (email == null || password == null) {
      context.response()
        .setStatusCode(400)
        .end("Missing email or password");
      return;
    }

    // Validate user against the database
    boolean userValid = validateUser(email, password);
    if (!userValid) {
      context.response()
        .setStatusCode(401)
        .end("Invalid credentials");
      return;
    }

    try {
      String token = JWTAuthProvider.generateToken(email, password);

      context.response()
        .setStatusCode(200)
        .putHeader("Content-Type", "application/json")
        .end(new JsonObject().put("token", token).encode());
    } catch (Exception e) {
      context.response()
        .setStatusCode(500)
        .end("Failed to generate token: " + e.getMessage());
    }
  }

  private boolean validateUser(String email, String password) {
    // Example query to validate user. Adjust according to your database schema.
    ExpressionList<User> query = Ebean.find(User.class)
      .where()
      .eq("email", email)
      .eq("password", password);

    return query.findOne() != null;
  }
}


