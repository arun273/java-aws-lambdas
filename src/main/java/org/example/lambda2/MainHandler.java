package org.example.lambda2;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class MainHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String path = request.getPath();
        String body = request.getBody();

        if ("/functionOne".equals(path)) {
            return new FunctionOne().handle(body, context);
        } else if ("/functionTwo".equals(path)) {
            return new FunctionTwo().handle(body, context);
        }

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(404)
                .withBody("Path not found: " + path);
    }
}