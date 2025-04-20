package com.example.lambda;

public class HttpHandlerImpl implements IHttpHandler {
    public HttpHandlerImpl() {
        // Simulate HTTP client setup
        System.out.println("HttpHandlerImpl initialized.");
    }

    @Override
    public String callExternalApi(String input) {
        System.out.println("Calling external API with: " + input);
        return "Response for: " + input;
    }
}
