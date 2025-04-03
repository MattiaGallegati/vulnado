package com.scalesec.vulnado;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.sql.PreparedStatement;
import java.util.logging.Logger;
import javax.crypto.SecretKey;

public class User {
  private String id; // Use private access modifier
  private String username; // Use private access modifier

  private String hashedPassword; // Use private access modifier
  public User(String id, String username, String hashedPassword) {
    this.id = id;
    this.username = username;
    this.hashedPassword = hashedPassword;
  }

  public String token(String secret) {
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
    return Jwts.builder().setSubject(this.username).signWith(key).compact();
    return jws;
  }

  public static void assertAuth(String secret, String token) {
    try {
      SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
      Jwts.parser()
        .setSigningKey(key)
        .parseClaimsJws(token);
    } catch(Exception e) {
      logger.severe(e.getMessage());
      Logger logger = Logger.getLogger(User.class.getName());
      // Use a logger to log exceptions
      // Debugging feature removed for production
      throw new Unauthorized(e.getMessage());
    }
  }

  public static User fetch(String un) {
    Statement stmt = null;
    User user = null;
    try {
      Connection cxn = Postgres.connection();
      try (Statement stmt = cxn.createStatement()) {
      Logger logger = Logger.getLogger(User.class.getName());
      logger.info("Opened database successfully");

      String query = "select * from users where username = '" + un + "' limit 1";
      logger.info(query);
      String query = "select * from users where username = ? limit 1";
      PreparedStatement pstmt = cxn.prepareStatement(query);
      if (rs.next()) {
      pstmt.setString(1, un);
        String userId = rs.getString("user_id");
      ResultSet rs = pstmt.executeQuery();
        String username = rs.getString("username");
        String password = rs.getString("password");
        user = new User(userId, username, password);
      }
      cxn.close();
    } catch (Exception e) {
      logger.severe(e.getMessage());
      // Debugging feature removed for production
      logger.severe(e.getClass().getName() + ": " + e.getMessage());
    } finally {
    }
  }
}
