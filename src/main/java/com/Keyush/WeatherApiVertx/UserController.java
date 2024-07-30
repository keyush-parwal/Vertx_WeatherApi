package com.Keyush.WeatherApiVertx;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
;

import java.util.List;

public class UserController implements Handler<RoutingContext> {

  private final UserService userService;

  public UserController(Vertx vertx, Router router) {
    userService = new UserService();

    router.post("/users").handler(this::createUser);
    router.get("/users/:id").handler(this::getUser);
    router.get("/users").handler(this::getAllUsers);
    router.put("/users/:id").handler(this::updateUser);
    router.delete("/users/:id").handler(this::deleteUser);
  }


  private void createUser(RoutingContext context) {
    try {
      // Decode the JSON body to a Student object
      User user = Json.decodeValue(context.getBodyAsString(), User.class);

      if (user.getName() ==null || user.getEmail() == null || user.getPassword() == null) {
        throw new IllegalArgumentException("Missing parameters");
      }

      userService.createUser(user);
      context.response()
        .setStatusCode(201)
        .end(Json.encodePrettily(user));
    } catch (IllegalArgumentException e) {
      context.response()
        .setStatusCode(400)
        .end("Invalid input data: " + e.getMessage());
    } catch (Exception e) {
      context.response()
        .setStatusCode(500)
        .end("Internal Server Error: " + e.getMessage());
    }
  }

  private void getUser(RoutingContext context) {
    int id = Integer.parseInt(context.pathParam("id"));
    User user = userService.getUser(id);
    if (user != null) {
      context.response()
        .setStatusCode(200)
        .end(Json.encodePrettily(user));
    } else {
      context.response()
        .setStatusCode(404)
        .end();
    }
  }

  private void getAllUsers(RoutingContext context) {
    List<User> users = userService.getAllUsers();
    context.response()
      .setStatusCode(200)
      .end(Json.encodePrettily(users));
  }

  private void updateUser(RoutingContext context) {
    int id = Integer.parseInt(context.pathParam("id"));
    User user = Json.decodeValue(context.getBodyAsString(), User.class);
    user.setId(id);
    userService.updateUser(user);
    context.response()
      .setStatusCode(200)
      .end(Json.encodePrettily(user));
  }

  private void deleteUser(RoutingContext context) {
    int id = Integer.parseInt(context.pathParam("id"));
    userService.deleteUser(id);
    context.response()
      .setStatusCode(204)
      .end();
  }

  @Override
  public void handle(RoutingContext event) {
  }
}

