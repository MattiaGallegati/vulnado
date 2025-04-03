package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinkListerTests {

    // Test for getLinks method
    @Test
    public void getLinks_ValidUrl_ShouldReturnLinks() throws IOException {
        // Arrange
        String testUrl = "http://example.com";
        LinkLister linkLister = Mockito.spy(LinkLister.class);

        // Mock Jsoup.connect(url).get() to return a document with links
        Document mockDocument = mock(Document.class);
        Elements mockElements = mock(Elements.class);
        Element mockElement = mock(Element.class);

        when(mockElement.absUrl("href")).thenReturn("http://example.com/link1");
        when(mockElements.iterator()).thenReturn(List.of(mockElement).iterator());
        when(mockDocument.select("a")).thenReturn(mockElements);
        doReturn(mockDocument).when(linkLister).getDocument(testUrl);

        // Act
        List<String> links = linkLister.getLinks(testUrl);

        // Assert
        assertNotNull("Links should not be null", links);
        assertEquals("Links size should be 1", 1, links.size());
        assertEquals("First link should match", "http://example.com/link1", links.get(0));
    }

    @Test(expected = IOException.class)
    public void getLinks_InvalidUrl_ShouldThrowIOException() throws IOException {
        // Arrange
        String invalidUrl = "http://invalid-url.com";

        // Act
        LinkLister.getLinks(invalidUrl);

        // Assert
        // Exception is expected
    }

    // Test for getLinksV2 method
    @Test
    public void getLinksV2_ValidUrl_ShouldReturnLinks() throws BadRequest {
        // Arrange
        String testUrl = "http://example.com";

        // Act
        List<String> links = LinkLister.getLinksV2(testUrl);

        // Assert
        assertNotNull("Links should not be null", links);
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_PrivateIp_ShouldThrowBadRequest() throws BadRequest {
        // Arrange
        String privateIpUrl = "http://192.168.1.1";

        // Act
        LinkLister.getLinksV2(privateIpUrl);

        // Assert
        // Exception is expected
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_InvalidUrl_ShouldThrowBadRequest() throws BadRequest {
        // Arrange
        String invalidUrl = "http://invalid-url.com";

        // Act
        LinkLister.getLinksV2(invalidUrl);

        // Assert
        // Exception is expected
    }

    // Helper method to mock Jsoup.connect(url).get()
    private Document mockDocument(String url) throws IOException {
        Document mockDocument = mock(Document.class);
        Elements mockElements = mock(Elements.class);
        Element mockElement = mock(Element.class);

        when(mockElement.absUrl("href")).thenReturn(url);
        when(mockElements.iterator()).thenReturn(List.of(mockElement).iterator());
        when(mockDocument.select("a")).thenReturn(mockElements);

        return mockDocument;
    }
}
