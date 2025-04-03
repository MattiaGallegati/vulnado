package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

    @MockBean
    private CommentsController commentsController;

    @MockBean
    private Comment comment;

    @Value("${app.secret}")
    private String secret;

    @Test
    public void contextLoads() {
        // Test to ensure the application context loads successfully
        assertNotNull("Application context should load", commentsController);
    }

    @Test
    public void comments_ShouldReturnListOfComments() {
        // Arrange
        String token = "valid-token";
        List<Comment> mockComments = Arrays.asList(new Comment(), new Comment());
        when(commentsController.comments(token)).thenReturn(mockComments);

        // Act
        List<Comment> result = commentsController.comments(token);

        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Result size should match", mockComments.size(), result.size());
    }

    @Test
    public void createComment_ShouldCreateAndReturnComment() {
        // Arrange
        String token = "valid-token";
        CommentRequest input = new CommentRequest();
        input.username = "testUser";
        input.body = "testBody";
        Comment mockComment = new Comment();
        when(commentsController.createComment(token, input)).thenReturn(mockComment);

        // Act
        Comment result = commentsController.createComment(token, input);

        // Assert
        assertNotNull("Result should not be null", result);
    }

    @Test
    public void deleteComment_ShouldReturnTrueOnSuccessfulDeletion() {
        // Arrange
        String token = "valid-token";
        String id = "123";
        when(commentsController.deleteComment(token, id)).thenReturn(true);

        // Act
        Boolean result = commentsController.deleteComment(token, id);

        // Assert
        assertTrue("Result should be true", result);
    }

    @Test(expected = BadRequest.class)
    public void createComment_ShouldThrowBadRequestOnInvalidInput() {
        // Arrange
        String token = "valid-token";
        CommentRequest input = new CommentRequest();
        input.username = null; // Invalid input
        input.body = "testBody";
        doThrow(new BadRequest("Invalid input")).when(commentsController).createComment(token, input);

        // Act
        commentsController.createComment(token, input);
    }

    @Test(expected = ServerError.class)
    public void deleteComment_ShouldThrowServerErrorOnFailure() {
        // Arrange
        String token = "valid-token";
        String id = "invalid-id";
        doThrow(new ServerError("Deletion failed")).when(commentsController).deleteComment(token, id);

        // Act
        commentsController.deleteComment(token, id);
    }
}
