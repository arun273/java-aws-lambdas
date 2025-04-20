package com.example.lambda;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceFactoryTest {

    @Test
    void testServiceInstanceIsNotNull() {
        MyService service = ServiceFactory.getService();
        assertNotNull(service, "ServiceFactory returned null service instance");
    }

    @Test
    void testServiceInstanceIsSingleton() {
        MyService instance1 = ServiceFactory.getService();
        MyService instance2 = ServiceFactory.getService();
        assertSame(instance1, instance2, "ServiceFactory should return the same singleton instance");
    }

    @Test
    void testServiceProcessCallAfterInit() {
        MyService service = ServiceFactory.getService();
        String result = service.process("hello");
        assertTrue(result.contains("Processed:"), "Service should process the input correctly");
    }

    @Test
    void testServiceInitNotFailingOptimistically() {
        try {
            MyService service = ServiceFactory.getService();
            service.init(); // should be idempotent and not crash
            assertTrue(true); // passed without exception
        } catch (Exception e) {
            fail("Service init should not throw exception during optimistic eager loading");
        }
    }

    @Test
    void testMultipleCallsStillUseInitializedState() {
        MyService service = ServiceFactory.getService();
        String first = service.process("first");
        String second = service.process("second");

        assertNotEquals(first, second, "Service should handle different inputs independently");
        assertTrue(first.startsWith("Processed:"));
        assertTrue(second.startsWith("Processed:"));
    }
}
