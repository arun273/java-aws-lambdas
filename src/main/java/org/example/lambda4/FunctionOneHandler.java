package org.example.lambda4;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class FunctionOneHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final DynamoDBClient dynamoDBClient = new DynamoDBClient();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String key = request.getQueryStringParameters().get("key");

        var item = dynamoDBClient.getItem("MyTable", "PrimaryKey", key);

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody("Retrieved item: " + item.toString());
    }
}
