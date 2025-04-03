package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VulnadoApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
        // Ensures the application context loads successfully
        assertNotNull("Application context should load", restTemplate);
    }

    @Test
    public void CowController_Cowsay_ShouldReturnDefaultMessage() {
        // Test the default message returned by the cowsay endpoint
        ResponseEntity<String> response = restTemplate.getForEntity("/cowsay", String.class);
        assertEquals("Default message should be returned", Cowsay.run("I love Linux!"), response.getBody());
    }

    @Test
    public void CowController_Cowsay_ShouldReturnCustomMessage() {
        // Test the custom message returned by the cowsay endpoint
        String customMessage = "Hello, World!";
        ResponseEntity<String> response = restTemplate.getForEntity("/cowsay?input=" + customMessage, String.class);
        assertEquals("Custom message should be returned", Cowsay.run(customMessage), response.getBody());
    }

    @Test
    public void CowController_Cowsay_ShouldHandleEmptyInput() {
        // Test the behavior when an empty input is provided
        ResponseEntity<String> response = restTemplate.getForEntity("/cowsay?input=", String.class);
        assertEquals("Default message should be returned for empty input", Cowsay.run("I love Linux!"), response.getBody());
    }
}
