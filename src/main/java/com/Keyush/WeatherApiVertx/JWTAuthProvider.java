package com.Keyush.WeatherApiVertx;

import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.core.Vertx;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JWTAuthProvider {
  private static final String SECRET_KEY = "qwertyuiopasdfghjklzxcvbnmqwertyuiopjnefgnrjnvnvnrbjrnlvjnblrkjnblkoueingnrr";
  private static final long EXPIRATION_TIME = 3600000; // 1 hour in milliseconds

  private static JWTAuth jwtAuth;

  public static JWTAuth getJwtAuth(Vertx vertx) {
    if (jwtAuth == null) {
      JWTAuthOptions config = new JWTAuthOptions()
        .addPubSecKey(new PubSecKeyOptions()
          .setAlgorithm("HS256")
          .setBuffer(SECRET_KEY));

      jwtAuth = JWTAuth.create(vertx, config);
    }
    return jwtAuth;
  }

  public static String generateToken(String email, String password) {
    return Jwts.builder()
      .setSubject(email)
      .claim("password", password)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
      .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
      .compact();
  }
}
