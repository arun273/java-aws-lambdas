package com.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaHandler implements RequestHandler<String, String> {

    private final MyService service;

    // Default constructor used in real Lambda runtime
    public LambdaHandler() {
        this(ServiceFactory.getService());
    }

    // For testing
    public LambdaHandler(MyService service) {
        this.service = service;
    }

    @Override
    public String handleRequest(String input, Context context) {
        return service.process(input);
    }
}
