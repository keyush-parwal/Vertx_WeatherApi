package com.Keyush.WeatherApiVertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;

public class MainVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }

  @Override
  public void start() {
    DBConfig.setup();
    ApiClient apiClient = new ApiClient();

    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    JWTAuthProvider jwtAuthProvider = new JWTAuthProvider();
    JWTAuth jwtAuth = jwtAuthProvider.getJwtAuth(vertx);

    // Add JWTAuthHandler to the router for secured routes
    router.route("/weather/forecast/summary/*").handler(JWTAuthHandler.create(jwtAuth));

    // Auth
    new AuthController(vertx, router);

    // CRUD
    new UserController(vertx, router);

    WeatherHandler weatherHandler = new WeatherHandler(apiClient);

    router.get("/weather/forecast/summary/:city").handler(weatherHandler::getWeatherForecastSummary);
    router.get("/weather/forecast/hourly/:city").handler(weatherHandler::getHourlyForecast);
    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8888, http -> {
        if (http.succeeded()) {
          System.out.println("HTTP server started on port 8888");
        } else {
          System.out.println("HTTP server failed to start");
        }
      });
  }

}

