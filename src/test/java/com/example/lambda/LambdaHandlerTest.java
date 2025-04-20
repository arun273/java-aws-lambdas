package com.example.lambda;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LambdaHandlerTest {

    @Test
    void testHandleRequestWithInjectedService() {
        MyService mockService = mock(MyService.class);
        when(mockService.process("event")).thenReturn("Processed: mocked");

        LambdaHandler handler = new LambdaHandler(mockService);
        String response = handler.handleRequest("event", null);

        assertEquals("Processed: mocked", response);
        verify(mockService).process("event");
    }
}
