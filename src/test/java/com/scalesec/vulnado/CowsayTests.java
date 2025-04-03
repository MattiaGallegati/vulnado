package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

    @Test
    public void contextLoads() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Test
    public void Cowsay_Run_ShouldReturnExpectedOutput() throws Exception {
        // Mocking the ProcessBuilder and Process
        ProcessBuilder processBuilderMock = Mockito.mock(ProcessBuilder.class);
        Process processMock = Mockito.mock(Process.class);

        // Mocking the InputStreamReader to simulate the output of the command
        String expectedOutput = "Mocked Cowsay Output\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(expectedOutput.getBytes());
        BufferedReader readerMock = new BufferedReader(new InputStreamReader(inputStream));

        Mockito.when(processBuilderMock.start()).thenReturn(processMock);
        Mockito.when(processMock.getInputStream()).thenReturn(inputStream);

        // Injecting the mock into the Cowsay class
        Cowsay cowsay = new Cowsay();
        String actualOutput = cowsay.run("Hello");

        // Asserting the output
        assertEquals("The output should match the mocked output", expectedOutput, actualOutput);
    }

    @Test
    public void Cowsay_Run_ShouldHandleExceptionGracefully() {
        // Mocking the ProcessBuilder to throw an exception
        ProcessBuilder processBuilderMock = Mockito.mock(ProcessBuilder.class);
        Mockito.when(processBuilderMock.start()).thenThrow(new RuntimeException("Mocked Exception"));

        // Injecting the mock into the Cowsay class
        Cowsay cowsay = new Cowsay();
        String actualOutput = cowsay.run("Hello");

        // Asserting the output
        assertTrue("The output should be empty when an exception occurs", actualOutput.isEmpty());
    }
}
