package com.example.lambda;

public class MyService {

    private final IHttpHandler httpHandler;

    // Default constructor using default implementation
    public MyService() {
        this.httpHandler = new HttpHandlerImpl();
    }

    // Param constructor (for test/mocks)
    public MyService(IHttpHandler httpHandler) {
        this.httpHandler = httpHandler;
    }

    public void init() {
        System.out.println("Warming up MyService...");
        httpHandler.callExternalApi("warmup");
    }

    public String process(String input) {
        return "Processed: " + httpHandler.callExternalApi(input);
    }
}
