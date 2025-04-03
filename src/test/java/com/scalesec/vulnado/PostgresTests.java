package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

    @Test
    public void contextLoads() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Test
    public void Postgres_Connection_ShouldEstablishSuccessfully() {
        try {
            Connection connection = Postgres.connection();
            assertNotNull("Connection should not be null", connection);
            connection.close();
        } catch (Exception e) {
            fail("Connection failed: " + e.getMessage());
        }
    }

    @Test
    public void Postgres_Setup_ShouldCreateTablesAndSeedData() {
        try {
            Postgres.setup();
            Connection connection = Postgres.connection();
            Statement stmt = connection.createStatement();

            // Verify users table
            ResultSet rsUsers = stmt.executeQuery("SELECT COUNT(*) AS count FROM users");
            rsUsers.next();
            int userCount = rsUsers.getInt("count");
            assertEquals("Users table should have 5 seed entries", 5, userCount);

            // Verify comments table
            ResultSet rsComments = stmt.executeQuery("SELECT COUNT(*) AS count FROM comments");
            rsComments.next();
            int commentCount = rsComments.getInt("count");
            assertEquals("Comments table should have 2 seed entries", 2, commentCount);

            connection.close();
        } catch (Exception e) {
            fail("Setup failed: " + e.getMessage());
        }
    }

    @Test
    public void Postgres_InsertUser_ShouldAddUserToDatabase() {
        try {
            String username = "test_user_" + UUID.randomUUID();
            String password = "TestPassword123!";
            Postgres.insertUser(username, password);

            Connection connection = Postgres.connection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT username FROM users WHERE username = '" + username + "'");
            assertTrue("User should exist in the database", rs.next());
            assertEquals("Username should match", username, rs.getString("username"));

            connection.close();
        } catch (Exception e) {
            fail("Insert user failed: " + e.getMessage());
        }
    }

    @Test
    public void Postgres_InsertComment_ShouldAddCommentToDatabase() {
        try {
            String username = "test_user_" + UUID.randomUUID();
            String password = "TestPassword123!";
            Postgres.insertUser(username, password);

            String commentBody = "This is a test comment!";
            Postgres.insertComment(username, commentBody);

            Connection connection = Postgres.connection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT body FROM comments WHERE username = '" + username + "'");
            assertTrue("Comment should exist in the database", rs.next());
            assertEquals("Comment body should match", commentBody, rs.getString("body"));

            connection.close();
        } catch (Exception e) {
            fail("Insert comment failed: " + e.getMessage());
        }
    }

    @Test
    public void Postgres_MD5_ShouldGenerateCorrectHash() {
        String input = "TestPassword123!";
        String expectedHash = "cc03e747a6afbbcbf8be7668acfebee5"; // Precomputed MD5 hash
        String actualHash = Postgres.md5(input);
        assertEquals("MD5 hash should match expected value", expectedHash, actualHash);
    }
}
