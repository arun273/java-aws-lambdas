package org.example.lambda2;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class FunctionOne {

    public APIGatewayProxyResponseEvent handle(String body, Context context) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody("Function One processed: " + body);
    }
}