package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

	@Test
	public void contextLoads() {
        // Test to ensure the application context loads successfully
        // This is a basic sanity check for the Spring Boot application
        try {
            VulnadoApplication.main(new String[]{});
        } catch (Exception e) {
            throw new AssertionError("Application context failed to load", e);
        }
	}

    @Test
    public void main_ShouldCallPostgresSetup() {
        // Mocking Postgres class to verify setup method is called
        Postgres mockPostgres = Mockito.mock(Postgres.class);
        doNothing().when(mockPostgres).setup();

        // Call the main method
        VulnadoApplication.main(new String[]{});

        // Verify that Postgres.setup() was called
        verify(mockPostgres, times(1)).setup();
    }
}
