package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

    @Test
    public void contextLoads() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Test
    public void Comment_Create_ShouldReturnValidComment() {
        // Arrange
        String username = "testUser";
        String body = "This is a test comment";

        // Act
        Comment comment = Comment.create(username, body);

        // Assert
        assertNotNull("Comment should not be null", comment);
        assertEquals("Username should match", username, comment.username);
        assertEquals("Body should match", body, comment.body);
        assertNotNull("Timestamp should not be null", comment.created_on);
    }

    @Test(expected = BadRequest.class)
    public void Comment_Create_ShouldThrowBadRequestOnCommitFailure() throws SQLException {
        // Arrange
        String username = "testUser";
        String body = "This is a test comment";
        Comment mockComment = Mockito.spy(new Comment("mockId", username, body, new Timestamp(System.currentTimeMillis())));
        doThrow(new SQLException("Commit failed")).when(mockComment).commit();

        // Act
        mockComment.create(username, body);
    }

    @Test
    public void Comment_FetchAll_ShouldReturnListOfComments() throws SQLException {
        // Arrange
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(Postgres.connection()).thenReturn(mockConnection);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery("select * from comments;")).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString("id")).thenReturn("id1", "id2");
        when(mockResultSet.getString("username")).thenReturn("user1", "user2");
        when(mockResultSet.getString("body")).thenReturn("body1", "body2");
        when(mockResultSet.getTimestamp("created_on")).thenReturn(new Timestamp(System.currentTimeMillis()));

        // Act
        List<Comment> comments = Comment.fetch_all();

        // Assert
        assertNotNull("Comments list should not be null", comments);
        assertEquals("Comments list size should match", 2, comments.size());
        assertEquals("First comment username should match", "user1", comments.get(0).username);
        assertEquals("Second comment username should match", "user2", comments.get(1).username);
    }

    @Test
    public void Comment_Delete_ShouldReturnTrueOnSuccessfulDeletion() throws SQLException {
        // Arrange
        String id = "testId";
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(Postgres.connection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement("DELETE FROM comments where id = ?")).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Act
        boolean result = Comment.delete(id);

        // Assert
        assertTrue("Delete should return true on successful deletion", result);
    }

    @Test
    public void Comment_Delete_ShouldReturnFalseOnFailure() throws SQLException {
        // Arrange
        String id = "testId";
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(Postgres.connection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement("DELETE FROM comments where id = ?")).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        // Act
        boolean result = Comment.delete(id);

        // Assert
        assertFalse("Delete should return false on failure", result);
    }
}
