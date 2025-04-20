package com.example.lambda;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MyServiceTest {

    @Test
    void testProcess() {
        IHttpHandler mockHandler = mock(IHttpHandler.class);
        when(mockHandler.callExternalApi("data")).thenReturn("mocked-response");

        MyService service = new MyService(mockHandler);
        String result = service.process("data");

        assertEquals("Processed: mocked-response", result);
        verify(mockHandler).callExternalApi("data");
    }

    @Test
    void testInit() {
        IHttpHandler mockHandler = mock(IHttpHandler.class);
        MyService service = new MyService(mockHandler);

        service.init();
        verify(mockHandler).callExternalApi("warmup");
    }
}
